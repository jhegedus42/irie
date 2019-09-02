package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.GetStateSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Responses.GetStateResponse
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.{StateMapEntry, StateMapSnapshot, UntypedRef}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImpl
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import app.shared.initialization.testing.TestUsers
import io.circe.Decoder
import akka.actor.{ActorLogging, ActorSystem, Props}
import io.circe.Decoder.Result

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * This should provide a type safe access to the persistent actor.
  * Who is using this ?
  *  persistence Operations
  */
case class TypeSafeAccessToPersistentActorProvider() {

  def getActor(id: String, as: ActorSystem) = as.actorOf(props(id))

  def props(id: String): Props =
    Props(new PersistentActorImpl(id))

  val actorSystemForPersistentActor: ActorSystem = ActorSystem()

  val actor: ActorRef = getActor(
    "the_one_and_only_parsistent_actor",
    actorSystemForPersistentActor
    // note : this is different from the one which is used for akka-http,
    // that is, the one which takes and answers the HTTP queries
    // todo-later - anser question : is this a problem ?
    // what does this mean ?
  )

  implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystemForPersistentActor.dispatcher

  def getSnaphot: Future[StateMapSnapshot] = {

    import akka.pattern.ask
    import scala.concurrent.duration._
    ask(actor, GetStateSnapshot)(Timeout.durationToTimeout(1 seconds))
      .mapTo[GetStateResponse]
      .map(_.state)
  }

  def getEntityWithVersion[V <: EntityValue[V]](
      ref: RefToEntityWithVersion[V]
  )(
      implicit
      d: Decoder[Entity[V]]
  ): Future[Option[Entity[V]]] = {

    def snapshot2res(stateMapSnapshot: StateMapSnapshot): Option[Entity[V]] = {
      val res: Option[StateMapEntry] = stateMapSnapshot.getEntity(ref)
      def res2entity(sme: StateMapEntry): Option[Entity[V]] = {
        val json = sme.entityAsString.entityAsJSON.json
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
      implicit pa: TypeSafeAccessToPersistentActorProvider,
    d: Decoder[Entity[EV]]
  ): Future[Option[Entity[EV]]] = {

    def snapshot2res(stateMapSnapshot: StateMapSnapshot): Option[Entity[EV]] = {

//      val res: Option[StateMapEntry] = stateMapSnapshot.getEntity(ref)
//      val par2 : UntypedRef=
      val res: Option[StateMapEntry] = stateMapSnapshot.getEntityWithLatestVersion(ref)

      def res2entity(sme: StateMapEntry): Option[Entity[EV]] = {
        val json = sme.entityAsString.entityAsJSON.json
        d.decodeJson(json).toOption
      }
      res.flatMap(res2entity(_))
    }

    val sh: Future[StateMapSnapshot] = getSnaphot
    sh.map(snapshot2res(_))
  }
}
