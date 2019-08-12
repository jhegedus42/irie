package app.server

import app.server.httpServer.{HttpServer, PersistenceModule}

object Main extends App {

  val pm=PersistenceModule()

  val server=HttpServer(pm)

  val host: String = args(0)

  server.start(host)


  //  - step 1 DONE : make sbt to start the "App" (this object here, print "hello world")

  // todo-now - step 2 : start the "server" on localhost
  //  - use app/server/httpServer/HttpServer.scala:84

  //  - step 3 : serve simple static content (a simple .txt file from the www directory)
  //  - step 4 : serve "the react page"
  //  - step 5 : make SumIntView work in the react page

}
