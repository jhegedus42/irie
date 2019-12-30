package app.server

import akka.actor.ActorSystem
import app.server.httpServer.HttpServer
import com.typesafe.config.ConfigFactory

object Main extends App {

  implicit lazy val app = ActorSystem(
    "App"
  )

  val server = HttpServer(app)

  val host: String = if (args.length == 0) "localhost" else args(0)

  server.startServer(host)

}
