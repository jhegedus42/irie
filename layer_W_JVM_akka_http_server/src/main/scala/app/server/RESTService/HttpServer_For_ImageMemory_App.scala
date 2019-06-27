package app.server.RESTService

import akka.actor.Terminated
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import app.comm_model_on_the_server_side.simple_route.SumIntViewRoute_For_Testing
import app.shared.{InvalidUUIDinURLError, SomeError_Trait}
import app.shared.data.ref.{TypedRef, RefVal}
import app.shared.rest.routes.crudRequests.GetEntityRequest
//import app.server.RESTService.routes.entityCRUD.{CreateEntityRoute, GetAllEntitiesRoute, GetRoute, UpdateEntityRoute}
import app.server.RESTService.routes.views.ViewRoute
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
//import app.server.RESTService.routes.views.UserLineListViewRoute
import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
import app.shared.config.Config
import app.shared.data.model.Entity.Entity
import app.shared.data.model.{LineText, User, LineList}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

//import shapeless.Typeable

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

/**
  * Ez mit csinál ?
  * Lekeírja, hogy
  *   - hogyan kell lekezelni az adott route-okhoz érkező kéréseket és
  *   -
  */

trait HttpServer_For_ImageMemory_App {
  self: InterfaceToStateAccessor =>

  implicit lazy val isa: InterfaceToStateAccessor = this

  // magunkat mint implicitet behozzuk a scope-ba
  // ez baromsagnak tunik, vmi el van rontva ...
  // ezt az "architekturat" at kene valahogy rendezni...
  // de hogyan ? later - miutan becsajoztam a progival
  // egyaltalan mi a IandInak csinaljuk ezt ?
  // biztos valami teszteleses baromsag miatt

  import io.circe._
  import io.circe.generic.auto._

  val route: Route = routeDef

  def selfExp: HttpServer_For_ImageMemory_App with InterfaceToStateAccessor = self

  implicit lazy val system: ActorSystem = ActorSystem( "trait-Server" )

  def rootPageHtml: String

  implicit lazy val executionContext: ExecutionContextExecutor = system.dispatcher

  def shutdownActorSystem(): Future[Terminated] = system.terminate()

  /**
    *
    *
    *
    * This is a route that gets Ref and returns the corresponding Entity.
    * We need to test this with some CURL or something.
    *
    * URL parameters
    *
    * print to screen
    *
    *
    *
    * @tparam E
    * @return
    */
  def getGetEntityRoute[E <: Entity: ClassTag: Decoder: Encoder]: Route = {
    import akka.http.scaladsl.server.Directives._
    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    val pathStr: String = GetEntityRequest.pathForGetEntityRoute_serverSideCode
    println( pathStr )
    val route =
      path( pathStr ) {
        get {
          parameters( 'id ) {
            id =>
              {

                import app.shared.data.ref.UUID_Utils.UUID
                import scalaz._

                val refDis: InvalidUUIDinURLError \/ TypedRef[E] =
                  UUID
                    .validate_from_String( id ).map( x => {
                      println( s"id after validation from string=$x" )
                      val res = TypedRef.makeWithUUID[E]( x )
                      println( s"Ref from id = $res" )
                      res
                    } )

                println( s"refDis=$refDis" )
                val refDisDanger: TypedRef[E]            = refDis.toEither.right.get //CRAPPYCODE
                val fr:           Future[RefVal[E]] = isa.getEntity( refDisDanger ).map( x => x.toEither.right.get )



                import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
                import io.circe.generic.auto._
                import io.circe.{Decoder, Encoder}

//                complete( s"The color is '$id'." )
                complete( fr )
                // vkitol le kell kerni ezt az entity-t
              }
          }
        }
      }
    route
  }

  def crudEntityRoute[E <: Entity: ClassTag: Decoder: Encoder]: Route = {

//    new UpdateEntityRoute[E]().route ~
//      new CreateEntityRoute[E]().route ~
//      new GetAllEntitiesRoute[E].route ~
//      new GetRoute[E]().route

    getGetEntityRoute[E]
  }

  /**
    * This defines the routes that the server can "handle".
    * b7dd04db32254f069a76c952118e9ae3$5e45b350c3d7df91abb31d34817ad48226d70ff8
    *
    * @return the routes that the server can "handle"
    */
  def routeDef: Route = {

    val routeForSumIntView =
      ViewRoute.getRouteForView[SumIntView]() // copied from d7b8aea40f454a46af529da21328f1aa

    val result: Route =
      routeForSumIntView ~
        SumIntViewRoute_For_Testing.route ~
        crudEntityRoute[LineText] ~
        crudEntityRoute[LineList] ~
        crudEntityRoute[User] ~
        StaticStuff.staticRootFactory( rootPageHtml )

    result
  }

  def start(args: Array[String] ): Unit = {
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

    val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle( route, host, Config.port )

    // mac
    // val bindingFuture = Http().bindAndHandle( route, "192.168.2.50", Config.port )
    // server

    println( s"listening on $host:${Config.port}" )
  }

}
