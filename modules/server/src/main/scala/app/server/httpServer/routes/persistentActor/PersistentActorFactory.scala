package app.server.httpServer.routes.persistentActor

import akka.actor.{ActorRef, ActorSystem, Props}

case class PersistentActorFactory(
  actorSystemForPersistentActor: ActorSystem) {

  private def getActor(
    id: String,
    as: ActorSystem
  ) = as.actorOf(props(id))

  private def props(id: String): Props =
    Props(new PersistentActorImpl(id))

  //  val actoyrSystemForPersistentActor: ActorSystem = ActorSystem()

  val actor: ActorRef = getActor(
    "the_one_and_only_parsistent_actor",
    actorSystemForPersistentActor
    // note : this is different from the one which is used for akka-http,
    // that is, the one which takes and answers the HTTP queries
    // todo-later - anser question : is this a problem ?
    // what does this mean ?
  )

}
