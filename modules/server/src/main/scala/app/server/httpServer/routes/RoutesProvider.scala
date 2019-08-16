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
import app.server.httpServer.persistence.persistentActor.StateChange
import app.shared.dataModel.value.refs.Entity

import scala.concurrent.Future
import scala.reflect.ClassTag

private[httpServer] case class RoutesProvider(
    persistenceModule: PersistenceModule
) {
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

  private def crudEntityRoute[E <: EntityValue[E]: ClassTag: Decoder: Encoder]
    : Route = {
    //    new UpdateEntityRoute[E]().route ~

    // todo-next - 0 - crudEntityRoute
    //  add getCreateEntityRoute's "result"

    getGetEntityRoute[E] //todo-now => make this "work"
  }

  private def getCreateEntityRoute[V <: EntityValue[V]:ClassTag]
  ( implicit encoder: Encoder[Entity[V]] ) : Route = {

    val value: V = ??? // get this from request

    // todo-next - getCreateEntityRoute
    //  we get this from the post request (extract)
    //    we need to write a simple test for that (extracting the value from
    //    the post requestion) using the akka route tester (we need to
    //    look that up in the akka http documentation)

    val toReturnAsResponse: Future[( StateChange, Entity[V] )] =
      persistenceModule.createAndStoreNewEntity( value )

    // todo-next
    //   - extract Future[Entity[V]] from `toReturnAsResponse` using a map
    //  -  we return that Entity[V] as response in a `complete`
    //   directive as it is done in the `getGetEntityRoute` function
    //   below



    ???
  }

  private def getGetEntityRoute[V <: EntityValue[V]: ClassTag: Decoder: Encoder]
    : Route = {
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
