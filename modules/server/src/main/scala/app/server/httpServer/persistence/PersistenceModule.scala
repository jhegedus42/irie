package app.server.httpServer.persistence

import akka.actor.{ActorRef, ActorSystem}
import app.server.httpServer.persistence.persistentActor.{AppPersistentActor, PersActorWrapper, StateChange}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{Entity, RefToEntity}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[httpServer] case class PersistenceModule( actorSystem: ActorSystem ) {

  implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  private val pa: ActorRef =
    AppPersistentActor.getActor( "one_and_only_parsistent_actor" )
  private val paw: PersActorWrapper = PersActorWrapper( pa )

  def getEntity[V <: EntityValue[V]]( uuid: String ): Future[Entity[V]] =
    ??? // todo-next fix this

  def createAndStoreNewEntity[V <: EntityValue[V]:ClassTag](
      value: V
  ): Future[(StateChange,Entity[V])] = {

    // todo-now
    //  step 1. create new entity from Value

    val entity: Entity[V] = Entity.makeFromValue( value )

    // todo-now
    //  step 2.
    //  implement : app.server.httpServer.persistence.persistentActor.PersActorWrapper.insertEntity

    val res: Future[StateChange] = paw.insertEntity[V]( entity )

    res.map( x => (x,entity) )
  }

}
