//package app.server.RESTService.routes.entityCRUD
//
//import akka.http.scaladsl.server.Route
////import app.server.RESTService.routes.entityCRUD.common.GetRouteBase
//import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
//import app.shared.SomeError_Trait
//import app.shared.data.model.Entity.{Data, Entity}
//import app.shared.data.ref.Ref
//import app.shared.data.ref.uuid.UUID
//import app.shared.rest.routes.crudRequests.GetEntityRequest
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//import io.circe.{Decoder, Encoder}
//
//import scala.concurrent.{ExecutionContext, Future}
//import scala.reflect.ClassTag
//
//class GetRoute[E <: Entity](
//  )(
//    implicit
//    i:  ClassTag[E],
//    en: Encoder[E],
//    de: Decoder[E],
//    sa: InterfaceToStateAccessor,
//    ec: ExecutionContext)
//    extends GetRouteBase[E] {
//
//// NOW DONE : fix this
//  override val command: GetEntityRequest[E] = GetEntityRequest[E]
//
//  override def processCommand(f: command.Params ): Future[command.Result] = {
//    import scalaz._
//    //ezzel elkeruljuk a wrong type hibat
//
//    val refDis: \/[SomeError_Trait, Ref[E]] =
//      UUID
//        .validate_from_String( f )
//        .map( Ref.makeWithUUID[E]( _ ) )
//
//    val fr: Future[command.Result] = refDis
//      .map( sa.getEntity( _ ) )
//      .fold( i => Future( (-\/( i ) ) ), i => i )
//    fr
//  }
//
//  import akka.http.scaladsl.server.Route
////  import akka.http.scaladsl.server.directives.MethodDirectives.get
////  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
////  import akka.http.scaladsl.server.directives.PathDirectives.path
//  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
//  override def route: Route = {
//
//
//    import akka.http.scaladsl.server.directives.RouteDirectives.complete
//
//    import akka.http.scaladsl.server.Directives._
//
//    get {
//      path( "test" ) {
//        complete( "IandI" )
//      }
//    } ~
//      cors() {
//        get {
//          path( command.getServerPath ) {
//            parameters( 'id ) {
//              id: String =>
//                completeRoute( processCommand _ )( id )
//
//            }
//          }
//        }
//      }
//
//  }
//}
