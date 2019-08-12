package app.server

import app.server.httpServer.{HttpServer, PersistenceModule}

object Main extends App {

  val pm=PersistenceModule()

  val server=HttpServer(pm)

  val host: String = args(0)

  server.start(host)

}
