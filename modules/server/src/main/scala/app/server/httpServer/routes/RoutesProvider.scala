package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.dynamic.RequestRoute
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state.StateChange
import app.server.httpServer.routes.static.{IndexDotHtml, StaticRoutes}
import app.shared.comm.requests.SumIntPostRequest
import app.shared.entity.{Entity, RefToEntity}
import app.shared.entity.entityValue.values.{Note, NoteFolder, User}
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future
import scala.reflect.ClassTag

private[httpServer] case class RoutesProvider(actorSystem: ActorSystem) {
  val persistenceModule=PersistenceModule(actorSystem)
  val route: Route = allRoutes

  import io.circe._
  import io.circe.generic.auto._

  private def allRoutes: Route = {


    val result: Route =
        RequestRoute.getRoute[SumIntPostRequest]().route
        crudEntityRoute[Note] ~
        crudEntityRoute[NoteFolder] ~
        crudEntityRoute[User] ~
        StaticRoutes.staticRootFactory( rootPageHtml )

    result
  }

  private def rootPageHtml: String = IndexDotHtml.getIndexDotHTML

  private def crudEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]]
  ): Route = {
    //  todo-next create entity route
    //  todo-next update entity route

    getGetEntityRoute[V] //todo-now 0 => make this "work"
  }

  private def getGetEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]]
  ): Route = {


//    val rr=RequestRoute[]

//    import akka.http.scaladsl.server.Directives._
//    val pathStr: String = GetEntityURLs.pathForGetEntityRoute_serverSideCode
//
//    println( pathStr )
//    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//    import io.circe.generic.auto._
//
//    val route: Route =
//      get {
//        path( pathStr ) {
//          //          parameters( 'json ) { uuid: String =>
//          entity( as[RefToEntity[V]] ) { refToEntity: RefToEntity[V] =>
//            val untypedRef: UntypedRef =
//              UntypedRef.makeFromRefToEntity( refToEntity )
//
//            complete(
//              persistenceModule.getEntity[V]( untypedRef )
//            )
//          }
//        }
//      }
//    route
    ???
  }


}
