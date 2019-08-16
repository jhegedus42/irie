package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.persistence.PersistenceModule
import app.server.httpServer.routes.staticContent.{IndexDotHtml, StaticRoutes}
import app.shared.comm.GetEntityURLs
import app.shared.dataModel.model.{Note, NoteFolder, User}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.views.SumIntView
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.shared.dataModel.value.refs.Entity

import scala.reflect.ClassTag

private[httpServer] case class RoutesProvider(persistenceModule: PersistenceModule) {
  val route: Route = allRoutes

  import io.circe._
  import io.circe.generic.auto._



  private def rootPageHtml: String = IndexDotHtml.getIndexDotHTML



  private def allRoutes: Route = {

    val routeForSumIntView =
      ViewRoute
        .getRouteForView[SumIntView]()

    val result: Route =
      routeForSumIntView ~
        crudEntityRoute[Note] ~
        crudEntityRoute[NoteFolder] ~
        crudEntityRoute[User] ~
        StaticRoutes.staticRootFactory( rootPageHtml )

    result
  }


  private def crudEntityRoute[E <: EntityValue[E]: ClassTag: Decoder: Encoder]: Route = {

    //    new CreateEntityRoute[E]().route ~
    //  todo-now - center step - 0

    //    new UpdateEntityRoute[E]().route ~

    getGetEntityRoute[E]
  }

  private def getCreateEntityRoute[V<:EntityValue[V]] : Route = ???
    //   todo-now - center step - 1
    //      => azt akarjuk, hogy legyen egy route, ami-re ha elkuldunk, egy value-t
    //         akkor azt akarjuk, hogy adjon nekunk vissza egy entity-t
    //         es azt az entity-t tarolja id el
    //           => ehhez kell egy olyan valami ami value-bol csinal entity-t, az hol van ?
    //              => DONE itt van : app.shared.dataModel.value.refs.Entity.makeFromValue
    //           => ami hatra marad, az az, hogy megirjuk ezt :
    //              app/server/httpServer/persistence/PersistenceModule.scala:18
    //
    //


  private def getGetEntityRoute[V <: EntityValue[V]: ClassTag: Decoder: Encoder]: Route = {
    import akka.http.scaladsl.server.Directives._
    val pathStr: String = GetEntityURLs.pathForGetEntityRoute_serverSideCode
    println( pathStr )

    val route =
      path( pathStr ) {
        get {
          parameters( 'uuid ) { uuid: String =>
          {
            import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
            import io.circe.generic.auto._
            complete(

              persistenceModule.getEntity[V]( uuid )
            )
          }
          }
        }
      }

    route
  }


}
