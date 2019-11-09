package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.persistentActor.PersistentActorWhisperer
import app.server.httpServer.routes.sodium.SodiumCRUDRoute
import app.server.httpServer.routes.sodium.SodiumExampleRoutes.GetAllUsersSodiumRoute
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import dataModel.{Image, Note, User}

import scala.concurrent.ExecutionContextExecutor

private[httpServer] case class RouteFactory(
  actorSystem: ActorSystem) {

  private implicit lazy val executionContext
    : ExecutionContextExecutor =
    actorSystem.dispatcher

  implicit val paw = PersistentActorWhisperer(actorSystem)

  val route: Route = allRoutes

  import io.circe.generic.auto._


  private def allRoutes: Route =
      getStaticRoute(rootPageHtml) ~
      GetAllUsersSodiumRoute(paw).getRoute


  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML

}
