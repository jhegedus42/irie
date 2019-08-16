package app.server

import akka.actor.ActorSystem
import app.server.httpServer.HttpServer

object Main extends App {

  implicit lazy val actorSystem: ActorSystem =
    ActorSystem( "ActorSystem for all Actors in the app." )

  val server=HttpServer(actorSystem)

  val host: String = args(0)

  server.startServer(host)

}

