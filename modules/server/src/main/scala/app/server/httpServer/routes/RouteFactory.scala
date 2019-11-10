package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import dataModel.{Image, Note, User}

import scala.concurrent.ExecutionContextExecutor

case class RouteFactory(
  implicit actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {

  val route: Route = allRoutes

  import io.circe.generic.auto._

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) //~
//      GetAllUsersSodiumRoute(paw).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

}
