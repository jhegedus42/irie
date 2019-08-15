package app.server.httpServer.persistence

import akka.actor.ActorRef
import app.server.httpServer.persistence.persistentActor.{AppPersistentActor, PersActorWrapper}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{Entity, RefToEntity}

import scala.concurrent.Future

private[httpServer] case class PersistenceModule() {

  private val pa: ActorRef =AppPersistentActor.getActor("one_and_only_parsistent_actor")
  private val paw: PersActorWrapper =PersActorWrapper(pa)

  def getEntity[E<:RefToEntity[E]](uuid:String):Future[Entity[E]] = ???

  def createAndStoreNewEntity[V<:EntityValue[V]](value: EntityValue[V]): Future[Entity[V]] = {

    // todo-now
    //  step 1. create new entity from Value

    val entity: Entity[EntityValue[V]] = Entity.makeFromValue(value)

    // todo-now
    //  step 2.
    //  implement : app.server.httpServer.persistence.persistentActor.PersActorWrapper.insertEntity


    ??? //continue here
  }


}
