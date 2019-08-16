package app.server.httpServer.persistence

import akka.actor.{ActorRef, ActorSystem}
import app.server.httpServer.persistence.persistentActor.{AppPersistentActor, PersActorWrapper, StateChange}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{Entity, RefToEntity}
import io.circe.Encoder

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

  def createAndStoreNewEntity[V <: EntityValue[V]: ClassTag](
      value: V
  )( implicit encoder: Encoder[Entity[V]])
  : Future[( StateChange, Entity[V] )] = {

    val entity: Entity[V] = Entity.makeFromValue( value )

    val res: Future[StateChange] = paw.insertEntity[V]( entity )

    res.map( x => ( x, entity ) )
  }

}
