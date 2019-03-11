//package app.server.RESTService.routes.entityCRUD
//
//import app.server.RESTService.routes.entityCRUD.common.GetRouteBase
//import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
//import app.shared.data.model.Entity.Entity
//import app.shared.rest.routes.crudRequests.GetAllEntitiesRequest
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//import io.circe.{Decoder, Encoder}
//
//import scala.concurrent.{ExecutionContext, Future}
//import scala.reflect.ClassTag
//case class GetAllEntitiesRoute[E <: Entity](
//  )(
//    implicit
//    i:  ClassTag[E],
//    en: Encoder[E],
//    de: Decoder[E],
//    sa: InterfaceToStateAccessor,
//    ec: ExecutionContext
//)
//    extends GetRouteBase[E] {
//
//  override val command: GetAllEntitiesRequest[E] = GetAllEntitiesRequest[E]()
//
//  override def processCommand(f: command.Params ): Future[command.Result] =
//    sa.getAllEntitiesOfGivenType[E]
//
//  import akka.http.scaladsl.server.Route
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.PathDirectives.path
//  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
//
//  override def route: Route =
//    cors() {
//      get {
//        path( command.getServerPath ) {
//          completeRoute( processCommand _ )( () )
//
//        }
//      }
//    }
//}
