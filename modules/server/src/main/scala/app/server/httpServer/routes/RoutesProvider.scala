package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.persistence.PersistenceModule
import app.server.httpServer.routes.staticContent.{IndexDotHtml, StaticRoutes}
import app.shared.comm.GetEntityURLs
import app.shared.dataModel.model.{Note, NoteFolder, User}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.views.SumIntPostRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.persistence.persistentActor.StateChange
import app.server.httpServer.persistence.persistentActor.state.UntypedRef
import app.shared.dataModel.value.refs.{Entity, RefToEntity}

import scala.concurrent.Future
import scala.reflect.ClassTag
import io.circe.generic.auto._

private[httpServer] case class RoutesProvider(
    persistenceModule: PersistenceModule
) {
  val route: Route = allRoutes

  import io.circe._
  import io.circe.generic.auto._

  private def allRoutes: Route = {

    val routeForSumIntView =
      PostRequestRoute
        .getRouteForPostRequest[SumIntPostRequest]()

    val result: Route =
      routeForSumIntView ~
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
    //    new UpdateEntityRoute[E]().route ~
    // todo-next - 0 - crudEntityRoute
    //  add getCreateEntityRoute's "result"

    getGetEntityRoute[V] //todo-now 0 => make this "work"
  }

  private def getGetEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]]
  ): Route = {
    import akka.http.scaladsl.server.Directives._
    val pathStr: String = GetEntityURLs.pathForGetEntityRoute_serverSideCode

    println( pathStr )
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._

    val route: Route =
      get {
        path( pathStr ) {
          //          parameters( 'json ) { uuid: String =>
          entity( as[RefToEntity[V]] ) { refToEntity: RefToEntity[V] =>
            val untypedRef: UntypedRef =
              UntypedRef.makeFromRefToEntity( refToEntity )

            complete(
              persistenceModule.getEntity[V]( untypedRef )
            )
          }
        }
      }
    route
  }

  private def getCreateEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit encoder: Encoder[Entity[V]]
  ): Route = {

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




}
