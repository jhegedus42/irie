package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImpl
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestUsers
import io.circe.Decoder

import scala.concurrent.Future

/**
  * This should provide a type safe access to the persistent actor.
  * Who is using this ?
  *  persistence Operations
  */
case class TypeSafeAccessToPersistentActorProvider() {

  val actor: ActorRef = PersistentActorImpl.getActor(
    "the_one_and_only_parsistent_actor"
  )

  def getEntityWithVersion[V <: EntityValue[V]](
      ref: RefToEntityWithVersion[V]
  )(
      implicit
      d: Decoder[Entity[V]]
  ): Future[Option[Entity[V]]] = {
    // we need to get a not type safe entity from the actor
    // we need to turn that into a type safe one
    val res: Future[Some[Entity[User]]] =
      Future.successful(Some(TestUsers.aliceEntity_with_UUID0))
    res.asInstanceOf[Future[Option[Entity[V]]]]
    // ??? // todo-right-now  => fix this dummy implementation
  }
}


