package app.server

import akka.actor.ActorSystem
import app.server.httpServer.HttpServer

object Main extends App {

  implicit lazy val actorSystem: ActorSystem =
    ActorSystem( "ActorSystem_for_all_Actors_in_the_app_created_in_Main_App_this_is_not_test" )

  val server=HttpServer(actorSystem)

  val host: String = if(args.length==0) "localhost" else args(0)

  server.startServer(host)

}

