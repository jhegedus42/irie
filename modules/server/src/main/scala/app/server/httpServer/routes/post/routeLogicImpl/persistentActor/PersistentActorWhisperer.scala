package app.server.httpServer.routes.post.routeLogicImpl.persistentActor

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Commands.{
  GetStateSnapshot,
  InsertNewEntityCommand,
  ResetStateCommand,
  UpdateEntityCommand
}
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Responses.GetStateResponse
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.{
  StateMapSnapshot,
  UntypedEntity,
  UntypedRef
}
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.logic.{
  DidOperationSucceed,
  PersistentActorImpl
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{
  EntityDeletedFlag,
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import io.circe.{Decoder, Encoder}

import scala.concurrent.duration._
import app.shared.entity.asString.{
  EntityValueAsJSON,
  EntityValueTypeAsString
}
import app.shared.entity.entityValue.values.User
import com.sun.org.apache.bcel.internal.classfile.StackMapEntry

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

/**
  * This should provide a type safe access to the persistent actor.
  *
  * Who is using this ?
  *
  * Route Logic-s.
  *
  */
case class PersistentActorWhisperer(
  actorSystemForPersistentActor: ActorSystem
)(
  implicit
  executionContext: ExecutionContextExecutor) {

  val paFactory = PersistentActorFactory(
    actorSystemForPersistentActor
  )
  val actor = paFactory.actor

  object WriteOps {

    def resetTheState(): Future[String] = {

      val ic = ResetStateCommand

      ask(actor, ic)(Timeout.durationToTimeout(1 seconds))
        .mapTo[String]
    }

    def updateEntity[V <: EntityValue[V]: ClassTag](
      currentEntity: Entity[V],
      newValue:      V
    )(
      implicit encoder: Encoder[Entity[V]],
      eencoder:         Encoder[V]
    ): Future[Option[Entity[V]]] = {

//    val entity: Entity[V] = Entity.makeFromValue[V](value)

      val currentEntityUntyped: UntypedEntity =
        UntypedEntity.makeFromEntity(currentEntity)

      val newValueAsJSON: EntityValueAsJSON =
        EntityValue.getAsJson(newValue)

      import monocle.macros.syntax.lens._
      val newValueAsEntity = currentEntity
        .lens(_.entityValue)
        .set(newValue)
        .lens(_.refToEntity.entityVersion)
        .modify(_.inc())

      val ic: UpdateEntityCommand = UpdateEntityCommand(
        currentEntityUntyped,
        newValueAsEntity
      )

      val res = ask(actor, ic)(Timeout.durationToTimeout(1 seconds))
        .mapTo[DidOperationSucceed]
        .map(x => Some(newValueAsEntity))
      // let's assume it did :) // fix-this-later
      // todo-one-day - fix this unsafeness
      res
    }

    def insertNewEntity[V <: EntityValue[V]: ClassTag](
      value: V
    )(
      implicit encoder: Encoder[Entity[V]],
      eencoder:         Encoder[V]
    ): Future[Option[Entity[V]]] = {

      val entity: Entity[V] = Entity.makeFromValue[V](value)

      val ute: UntypedEntity          = UntypedEntity.makeFromEntity(entity)
      val ic:  InsertNewEntityCommand = InsertNewEntityCommand(ute)

      val res = ask(actor, ic)(Timeout.durationToTimeout(1 seconds))
        .mapTo[DidOperationSucceed]
        .map(x => Some(entity))
      // let's assume it did :) // fix-this-later
      // todo-one-day - fix this unsafeness
      res
    }

  }

  def getAllUserRefs
    : Future[List[RefToEntityWithoutVersion[User]]] = {

    val snapshot: Future[StateMapSnapshot] = getSnaphot

    val entityValueTypeAsString: EntityValueTypeAsString =
      EntityValueTypeAsString.make[User]

    val refs: Future[List[UntypedRef]] = snapshot.map(x => {
      x.getAllRefsWithGivenEntityType(entityValueTypeAsString)
    })

    val allUserRefsUntyped
      : Future[List[UntypedRef]] = refs

    val refsTyped: Future[List[RefToEntityWithoutVersion[User]]] =
      allUserRefsUntyped.map(
        x => x.map(UntypedRef.getTypedRef[User](_).stripVersion())
      )
    refsTyped
  }

  def getEntityWithVersion[V <: EntityValue[V]](
    ref: RefToEntityWithVersion[V]
  )(
    implicit d: Decoder[Entity[V]]
  ): Future[Option[Entity[V]]] = {

    def snapshot2res(
      stateMapSnapshot: StateMapSnapshot
    ): Option[Entity[V]] = {
      val res: Option[UntypedEntity] = stateMapSnapshot.getEntity(ref)
      def res2entity(sme: UntypedEntity): Option[Entity[V]] = {
        val json = sme.entityValueAsJSON.json
        d.decodeJson(json).toOption
      }
      res.flatMap(res2entity(_))
    }

    val sh: Future[StateMapSnapshot] = getSnaphot
    sh.map(snapshot2res(_))
  }

  def getEntityWithLatestVersion[EV <: EntityValue[EV]](
    ref: RefToEntityWithoutVersion[EV]
  )(
    implicit d: Decoder[EV]
  ): Future[Option[Entity[EV]]] = {

    def snapshot2res(
      stateMapSnapshot: StateMapSnapshot
    ): Option[Entity[EV]] = {

//      val res: Option[StateMapEntry] = stateMapSnapshot.getEntity(ref)
//      val par2 : UntypedRef=

      val res: Option[UntypedEntity] =
        stateMapSnapshot.getEntityWithLatestVersion(ref)

      def res2entity(sme: UntypedEntity): Option[EV] = {
        val json = sme.entityValueAsJSON.json
        val res: Option[EV] = d.decodeJson(json).toOption
        res
      }

      val value: Option[EV] = res.flatMap(res2entity(_))
      val r2: Option[RefToEntityWithVersion[EV]] =
        res.map(x => UntypedRef.getTypedRef[EV](x.untypedRef))
      val entity: Entity[EV] =
        Entity[EV](value.get, r2.get, EntityDeletedFlag(false))
      Some(entity) // todo-next ^^^ fix this "not nice" error handling
    }

    val sh: Future[StateMapSnapshot] = getSnaphot

    sh.map(snapshot2res(_))
  }

  private def getSnaphot: Future[StateMapSnapshot] = {
    ask(actor, GetStateSnapshot)(Timeout.durationToTimeout(1 seconds))
      .mapTo[GetStateResponse]
      .map(_.state)
  }
}
