package app.server.httpServer.persistence

import akka.actor.ActorRef
import app.server.httpServer.persistence.persistentActor.{AppPersistentActor, PersActorWrapper}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity

import scala.concurrent.Future

private[httpServer] case class PersistenceModule() {

  private val pa: ActorRef =AppPersistentActor.getActor("one_and_only_parsistent_actor")
  private val paw: PersActorWrapper =PersActorWrapper(pa)

  def getEntity[E<:EntityValue[E]](uuid:String)    :  Future[Entity[E]] = ???

  def createNewEntity[V<:EntityValue[V]](entity: EntityValue[V]): Future[Entity[V]] = {
    ??? //continue here
  }


}
