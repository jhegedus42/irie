//package app.server.RESTService.routes.entityCRUD.common
//
//import akka.http.scaladsl.marshalling.ToResponseMarshallable
//import akka.http.scaladsl.server.directives.RouteDirectives.complete
//import akka.http.scaladsl.server.{Directive0, Route}
//import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
//import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
//import app.shared.data.model.Entity.Data
////import app.shared.rest.routes.Command
//import io.circe.{Decoder, Encoder}
//
//import scala.concurrent.{ExecutionContext, Future}
//import scala.reflect.ClassTag
//
///**
//  * Created by joco on 09/12/2017.
//  */
//// !!!!!!!!!!!!!!!!!!!
//import io.circe.generic.auto._ // dont forget to import this in subclasses
//// !!!!!!!!!!!!!!!!!!!
//
//trait RouteImplicits[E] {
//  implicit val i:  ClassTag[E]
//  implicit val en: Encoder[E]
//  implicit val de: Decoder[E]
//  implicit val sa: InterfaceToStateAccessor
//  implicit val ec: ExecutionContext
//}
//
//trait RouteBase[E <: Data] {
//  RouteImplicits =>
//
//  val command: Command[E]
//
//  implicit def decoder[Ent <: Data: Decoder]: Decoder[command.Result] = implicitly[Decoder[command.Result]]
//
//  def completeRoute(
//      f:   command.Params => Future[command.Result]
//    )(par: command.Params
//    )(
//      implicit
//      _marschallerJ: command.Result => ToResponseMarshallable,
//      ec:            ExecutionContext
//    ): Route = {
//
//    val res: Future[command.Result] = f( par )
//
//    val res2: Future[ToResponseMarshallable] = res.map( _marschallerJ( _ ) )
//
//    val res3: ToResponseMarshallable = ToResponseMarshallable.apply( res2 )
//
//    complete( res2 )
//
//    // why not just send back a json ?
//    // why is this magic needed ?
//
//  }
//
//  def processCommand(f: command.Params ): Future[command.Result]
//
//  def route: Route
//
//  def postOrPutRoute(pop:Directive0)(
//      implicit
//      um:            FromRequestUnmarshaller[command.Params],
//      _marschallerJ: command.Result => ToResponseMarshallable,
//      ec:            ExecutionContext
//    ): Route = {
//
//    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
//    cors() {
//      import akka.http.scaladsl.server.Directives._
//      pop {
//        path( command.getServerPath ) {
//          entity( as[command.Params] ) {
//            (entityBody: command.Params) =>
//              completeRoute( processCommand( _ ) )( entityBody )
//          }
//        }
//      }
//    }
//  }
//
//}
