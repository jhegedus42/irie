package app.server.httpServer.routes.crud

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import app.server.httpServer.routes.crud.routes.PersCommandRouteFactory
import app.server.httpServer.routes.fileUploading.UploadFileRoute
import app.server.httpServer.routes.static.IndexDotHtml
import app.server.httpServer.routes.static.StaticRoutes._
import io.circe.{Decoder, Encoder}
import shared.communication.persActorCommands.Response
import shared.communication.persActorCommands.auth.QueryAuthWrapper
import shared.communication.persActorCommands.crudCMDs.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd,
  UpdateEntitiesPersActorCmd,
  UpdateEntityPersActCmd
}
import shared.communication.persActorCommands.generalCmd.AdminQuery

import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

case class RouteAssembler(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  actorMaterializer:        ActorMaterializer) {

  //
  // todo-now
  //  write route to provide hash on user if the password and login matches
  //
  //

  import QueryAuthWrapper._

  val enc: Encoder[GetAllEntityiesForUserPersActCmd] =
    implicitly[Encoder[GetAllEntityiesForUserPersActCmd]]
  import io.circe.generic.semiauto._
  val encR = deriveEncoder[Response[GetAllEntityiesForUserPersActCmd]]

  val decSimple: Decoder[GetAllEntityiesForUserPersActCmd] =
    implicitly[Decoder[GetAllEntityiesForUserPersActCmd]]

  val decQAuthWrapper
    : Decoder[QueryAuthWrapper[GetAllEntityiesForUserPersActCmd]] = {

    QueryAuthWrapper.decoder[GetAllEntityiesForUserPersActCmd](
      decSimple
    )

  }

  val actor: ActorRef = actorSystem.actorOf(
    Props(
      new PersistentActorImpl(
        "the_one_and_only_parsistent_actor"
      )
    )
  )

  val uploadFileRoute = UploadFileRouteImpl()

  val route: Route = allRoutes

  private def allRoutes: Route =
    getStaticRoute(rootPageHtml) ~
//      PersCommandRouteFactory[
//        UpdateEntitiesPersActorCmd
//      ](actor).getRoute ~
      PersCommandRouteFactory[
        GetAllEntityiesForUserPersActCmd
      ](actor, decQAuthWrapper, enc, encR).getRoute ~
//      PersCommandRouteFactory[InsertEntityPersActCmd](
//        actor
//      ).getRoute ~
//      PersCommandRouteFactory[UpdateEntityPersActCmd](
//        actor
//      ).getRoute ~
      uploadFileRoute.route //~
//      PersCommandRouteWithResponse[AdminQuery](
//        actor
//      ).getRoute

  private def rootPageHtml: String =
    IndexDotHtml.getIndexDotHTML
}
