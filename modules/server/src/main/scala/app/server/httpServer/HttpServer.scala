package app.server.httpServer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import app.server.Config
import app.server.httpServer.routes.crud.RouteAssembler

import scala.concurrent.{ExecutionContextExecutor, Future}

case class HttpServer(actorSystem: ActorSystem) {

  implicit val actorSystemAsImplicit = actorSystem

  implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  implicit val materializer = ActorMaterializer()

  val routes = RouteAssembler()

  def startServer(host: String): Unit = {

    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle(routes.route,
                           Config.configFromJSON.host,
                           Config.configFromJSON.port)

    println(s"listening on $host:${Config.configFromJSON.port}")
  }

}
