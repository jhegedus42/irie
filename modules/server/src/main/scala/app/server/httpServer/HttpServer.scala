package app.server.httpServer

import akka.actor.Terminated
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.ViewRoute
import app.server.httpServer.routes.staticContent.{IndexDotHtml, StaticRoutes}
import app.shared.Config
import app.shared.comm.GetEntityURLs
import app.shared.dataModel.value.refs.Entity
import app.shared.dataModel.value.EntityValue

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import app.shared.dataModel.model.{Note, NoteFolder, User}
import app.shared.dataModel.views.SumIntView



case class  HttpServer(persistenceModule:PersistenceModule) {
  val route: Route = routeDef

  import io.circe._
  import io.circe.generic.auto._


  implicit lazy val system: ActorSystem = ActorSystem( "trait-Server" )

  def rootPageHtml: String = IndexDotHtml.getIndexDotHTML

  implicit lazy val executionContext: ExecutionContextExecutor =
    system.dispatcher

  def shutdownActorSystem(): Future[Terminated] = system.terminate()

  def routeDef: Route = {

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

  def crudEntityRoute[E <: EntityValue[E]: ClassTag: Decoder: Encoder]: Route = {

    //    new CreateEntityRoute[E]().route ~ // todo-now - center step - 0
    //    new UpdateEntityRoute[E]().route ~
    getGetEntityRoute[E]
  }

  def getCreateEntityRoute[E] : Route = ???  // todo-now - center step - 1

  def getGetEntityRoute[E <: EntityValue[E]: ClassTag: Decoder: Encoder]: Route = {
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
                persistenceModule.getEntity[E]( uuid )
              )
            }
          }
        }
      }

    route
  }

  def start( host:String ): Unit = {

    implicit val materializer = ActorMaterializer()

    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle( route, host, Config.port )

    println( s"listening on $host:${Config.port}" )
  }

}


