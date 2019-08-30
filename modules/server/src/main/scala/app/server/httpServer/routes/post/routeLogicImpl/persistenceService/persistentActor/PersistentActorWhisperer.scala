package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImp

case class PersistentActorWhisperer() {

  val actor: ActorRef = PersistentActorImp.getActor(
    "the_one_and_only_parsistent_actor"
  )
}
