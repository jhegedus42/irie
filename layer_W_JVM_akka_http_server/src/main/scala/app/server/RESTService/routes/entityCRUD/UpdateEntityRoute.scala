//package app.server.RESTService.routes.entityCRUD
//
///**
//  * Created by joco on 17/12/2017.
//  */
//import akka.http.scaladsl.server.Route
//import app.server.RESTService.routes.entityCRUD.common.RouteBase
//import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
//import app.shared.data.model.Entity.Entity
//import app.shared.rest.routes.crudRequests.UpdateEntityRequest
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//import io.circe.{Decoder, Encoder}
//
//import scala.concurrent.{ExecutionContext, Future}
//import scala.reflect.ClassTag
//
//case class UpdateEntityRoute[E <: Entity](
//  )(
//    implicit
//    i:  ClassTag[E],
//    en: Encoder[E],
//    de: Decoder[E],
//    sa: InterfaceToStateAccessor,
//    ec: ExecutionContext)
//    extends RouteBase[E] {
//
//  override val command: UpdateEntityRequest[E] = UpdateEntityRequest[E]()
//
//  override def processCommand(f: command.Params ): Future[command.Result] =
//    sa.updateEntity( f )
//
//  import akka.http.scaladsl.server.Directives._
//  override def route: Route = postOrPutRoute(put)
//}
