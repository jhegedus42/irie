package app.server.httpServer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import app.server.httpServer.routes.RouteFactory
import app.server.initialization.Config

import scala.concurrent.{ExecutionContextExecutor, Future}

case class HttpServer( actorSystem: ActorSystem ) {

  val routes = RouteFactory( actorSystem )

  implicit val actorSystemAsImplicit = actorSystem

  def startServer( host: String ): Unit = {

    implicit lazy val executionContext: ExecutionContextExecutor =
      actorSystem.dispatcher

    implicit val materializer = ActorMaterializer()

    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle( routes.route, host, Config.getDefaultConfig.port )

    println( s"listening on $host:${Config.getDefaultConfig.port}" )
  }

}
