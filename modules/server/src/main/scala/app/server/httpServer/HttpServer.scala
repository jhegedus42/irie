package app.server.httpServer

import akka.actor.Terminated
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.ViewRoute
import app.server.httpServer.routes.staticContent.{IndexDotHtml, StaticRoutes}
import app.shared.Config
import app.shared.comm.GetEntityURLs
import app.shared.dataModel.entity.refs.TypedRefVal
import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.{Note, NoteFolder, User}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
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

  def crudEntityRoute[E <: Entity[E]: ClassTag: Decoder: Encoder]: Route = {

    //    new CreateEntityRoute[E]().route ~
    //    new UpdateEntityRoute[E]().route ~
    getGetEntityRoute[E]
  }

  def getGetEntityRoute[E <: Entity[E]: ClassTag: Decoder: Encoder]: Route = {
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

  def start( args: Array[String] ): Unit = {

    implicit val materializer = ActorMaterializer()

    // needed for the future flatMap/onComplete in the end
    // val bindingFuture = Http().bindAndHandle( route, "localhost", Config.port )

    var host: String = null
    if (args.length == 0) {
      host = "localhost"
    } else {
      host = args( 0 )
    }

    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle( route, host, Config.port )

    // mac
    // val bindingFuture = Http().bindAndHandle( route, "192.168.2.50", Config.port )
    // server

    println( s"listening on $host:${Config.port}" )
  }

}

case class PersistenceModule() {

  def getEntity[E<:Entity[E]](uuid:String) :Future[TypedRefVal[E]] = ???

}
