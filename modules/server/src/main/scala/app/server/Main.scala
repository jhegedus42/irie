package app.server

import app.server.httpServer.HttpServer
import app.server.httpServer.persistence.PersistenceModule

object Main extends App {


  val server=HttpServer()

  val host: String = args(0)

  server.startServer(host)

}

