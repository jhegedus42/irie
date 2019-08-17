package app.server.httpServer.persistence

import akka.actor.{ActorRef, ActorSystem}
import app.server.httpServer.persistence.persistentActor.state.{ApplicationStateMapEntry, UntypedRef}
import app.server.httpServer.persistence.persistentActor.{AppPersistentActor, PersistentActorWrapper, Responses, StateChange}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.asString.EntityAsJSON
import app.shared.dataModel.value.refs.{Entity, RefToEntity}
import io.circe.Encoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[httpServer] case class PersistenceModule(actorSystem: ActorSystem) {

  implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  private val persistentActorWrapper: PersistentActorWrapper = {
    val peristentActor: ActorRef =
      AppPersistentActor.getActor("one_and_only_parsistent_actor")
    PersistentActorWrapper(peristentActor)
  }

  def getEntity[V <: EntityValue[V]](
                                      ref: RefToEntity[V]
                                    ): Future[Entity[V]] = {

    val eventualGetStateResult: Future[Responses.GetStateResult] =
      persistentActorWrapper.getState

    val untypedRef: UntypedRef = UntypedRef.makeFromRefToEntity(ref)

    val r1: Future[Option[ApplicationStateMapEntry]] =
      eventualGetStateResult.map(r => r.state.map.get(untypedRef))

    val r2: Future[ApplicationStateMapEntry] = r1.flatMap((x: Option[ApplicationStateMapEntry]) => {
      x match {
        case Some(value) => Future.successful(value)
        case None => Future.failed(new Exception(""))
      }
    })

    val r3: Future[EntityAsJSON] = r2.map((x: ApplicationStateMapEntry) =>{
      x.entityAsString.entityAsJSON
    })

    ???
  }

  def createAndStoreNewEntity[V <: EntityValue[V] : ClassTag](
                                                               value: V
                                                             )(
                                                               implicit encoder: Encoder[Entity[V]]
                                                             ): Future[(StateChange, Entity[V])] = {

    val entity: Entity[V] = Entity.makeFromValue(value)

    val res: Future[StateChange] =
      persistentActorWrapper.insertEntity[V](entity)

    res.map(x => (x, entity))
  }

}
