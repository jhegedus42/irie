package app.server.httpServer

import akka.actor.Terminated
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import app.comm_model_on_the_server_side.simple_route.SumIntViewRoute_For_Testing
import app.server.httpServer.routes.ViewRoute
import app.shared.{InvalidUUIDinURLError, SomeError_Trait}
import app.shared.data.ref.{TypedRef, TypedRefVal}
import app.shared.rest.routes.crudRequests.GetEntityRequest
import app.testHelpersShared.data.TestDataLabel
//import app.server.RESTService.routes.entityCRUD.{CreateEntityRoute, GetAllEntitiesRoute, GetRoute, UpdateEntityRoute}
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
//import app.server.RESTService.routes.views.UserLineListViewRoute
import app.shared.config.Config
import app.shared.data.model.Entity.Entity
import app.shared.data.model.{Note, NoteFolder, User}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

//import shapeless.Typeable

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object Main extends App {
  println( "Starting App" )
}

case class  HttpServer_For_ImageMemory_App(persistenceModule:PersistenceModule) {
  val route: Route = routeDef

  import io.circe._
  import io.circe.generic.auto._

  def setTestStateRoute(label: TestDataLabel): Route = {

    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    cors() {
      post {
        path( "setTestState") {
          import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
          import io.circe.generic.auto._

          entity( as[TestDataLabel] ) { entityBody => // get payload/json/body ...
            complete(
               ??? // todo some logic that depends on label
            )
          }
        }
      }
    }
  }

  implicit lazy val system: ActorSystem = ActorSystem( "trait-Server" )

  def rootPageHtml: String = ??? // todo complete this

  implicit lazy val executionContext: ExecutionContextExecutor =
    system.dispatcher

  def shutdownActorSystem(): Future[Terminated] = system.terminate()

  def routeDef: Route = {

    val routeForSumIntView =
      ViewRoute
        .getRouteForView[SumIntView]() // copied from d7b8aea40f454a46af529da21328f1aa

    val result: Route =
      routeForSumIntView ~
        SumIntViewRoute_For_Testing.route ~
        crudEntityRoute[Note] ~
        crudEntityRoute[NoteFolder] ~
        crudEntityRoute[User] ~
        StaticStuff.staticRootFactory( rootPageHtml )

    result
  }

  def crudEntityRoute[E <: Entity[E]: ClassTag: Decoder: Encoder]: Route = {

    //    new CreateEntityRoute[E]().route ~
    //    new UpdateEntityRoute[E]().route ~
    //      new GetAllEntitiesRoute[E].route ~
    //      new GetRoute[E]().route
    getGetEntityRoute[E]
  }

  def getGetEntityRoute[E <: Entity[E]: ClassTag: Decoder: Encoder]: Route = {
    import akka.http.scaladsl.server.Directives._
    val pathStr: String = GetEntityRequest.pathForGetEntityRoute_serverSideCode
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
    // 4dc327e9dce94fcfa994ad032bdcd3dd$4c99b1ca2b825dfc2e311c49f3572327a7c77e8d

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

  def getEntity[E](uuid:String) :Future[TypedRefVal[E]] = ???
  def setState(s: Any): Route = ???

}
