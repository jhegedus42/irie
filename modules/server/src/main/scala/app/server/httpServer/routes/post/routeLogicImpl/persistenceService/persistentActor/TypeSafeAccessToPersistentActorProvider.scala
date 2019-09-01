package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.GetStateSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Responses.GetStateResponse
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.StateMapSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImpl
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestUsers
import io.circe.Decoder

import akka.actor.{ActorLogging, ActorSystem, Props}
import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * This should provide a type safe access to the persistent actor.
  * Who is using this ?
  *  persistence Operations
  */
case class TypeSafeAccessToPersistentActorProvider() {


    def getActor(id: String,as:ActorSystem) = as.actorOf(props(id))

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
    // we need to get a not type safe entity from the actor
    //  we need access to a persistent actor
    //  we need to send some messages to that persistent actor
    //  we have been doing this before
    //  we need to find where that code is
    //  it is somewhere in some inspiration

    // we need to turn that into a type safe one

    val res: Future[Some[Entity[User]]] =
      Future.successful(Some(TestUsers.aliceEntity_with_UUID0))
    res.asInstanceOf[Future[Option[Entity[V]]]]

    // we need to get some snapshot from the actor
    // ??? // todo-right-now  => fix this dummy implementation
    //         use getSnapshot and extract the entity from there ...
    //  de hogyan ?
    //  mi kell hozza ?
    //  mit akarok csinalni ?
    //
  }
}
