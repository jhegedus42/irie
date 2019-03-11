package app.comm_model_on_the_server_side.serverSide.akkaHttpWebServer

import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.Route
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
//  import akka.http.scaladsl.server.directives.PathDirectives.path
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.CirceUtils._
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{
  ViewHttpRouteName,
  ViewHttpRouteNameProvider,
  JSONContainingGetViewPar,
  JSONContainingOptRes
}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View1_HolderObject.{
  View1,
  View1_Par
}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View2_HolderObject.View2
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.{
  View,
  View1_HolderObject
}
import app.comm_model_on_the_server_side.serverSide.logic.ServerSideLogic.ServerLogicTypeClass
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder, Error}
import app.shared.rest.routes.crudRequests.GetEntityRequest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

import scala.reflect.ClassTag

case class JSON(string: String )

case class HttpServerOnTheInternet() {

  val view1_getViewRequestHandler: GetViewRequestHandler[View1] =
    new GetViewRequestHandler[View1]()

  val view2_getViewRequestHandler: GetViewRequestHandler[View2] =
    new GetViewRequestHandler[View2]()

  val view1_routeName: ViewHttpRouteName =
    view1_getViewRequestHandler.getGetViewHttpRouteName()

  val view2_routeName: ViewHttpRouteName =
    view2_getViewRequestHandler.getGetViewHttpRouteName()

  def serveRequest(
      getViewHttpRouteName: ViewHttpRouteName,
      requestPayload:       JSONContainingGetViewPar
    ): Option[JSONContainingOptRes] = {

    // case based on endpointName
//    println("HttpServerOnTheInternet.serveRequest is called with endpoint name: " +
//           getViewHttpRouteName)

    val res: Option[JSONContainingOptRes] = getViewHttpRouteName.name match {
      case view1_routeName.`name` => {
//        println("route for view1 is called")
        Some(
          view1_getViewRequestHandler
            .decodeJSON2Par_SendParToLogic_EncodeResultToJSON[View1]( requestPayload )
        )
      }

      case view2_routeName.`name` =>
        Some(
          view1_getViewRequestHandler
            .decodeJSON2Par_SendParToLogic_EncodeResultToJSON[View2]( requestPayload )
        )

      case _ => None
    }
//    println("the server returns: "+res)
    res
  }

}

// Random UUID: 1331df23fe1b4e8b989ad69bb844ce58
// commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
// Date: Sun Sep  2 18:39:13 EEST 2018

case class GetViewRequestHandler[V <: View: ClassTag]() {

  def decodeJSON2Par_SendParToLogic_EncodeResultToJSON[V <: View](
      paramJSON: JSONContainingGetViewPar
    )(
      implicit
      decoder:     Decoder[V#Par],
      encoder:     Encoder[V#Res],
      serverLogic: ServerLogicTypeClass[V]
    ): JSONContainingOptRes = {

//    println("GetViewRequestHandler's decodeJSON2Par_SendParToLogic_EncodeResultToJSON is called " +
//            "for the " + getGetViewHttpRouteName() + " route, with parameter: "+paramJSON)

    val r: Either[Error, V#Par] = decodeJSONToPar[V]( paramJSON )

    val parOpt: Option[V#Par] = r.right.toOption

    val resOpt: Option[V#Res] = for {
      par <- parOpt
      res <- serverLogic.getView( par )
    } yield res

//    println("it's results is (before encoding it):"+resOpt)

    (encodeOptResToJSONContainingOptRes[V]( resOpt ) )

  }

  // Random UUID: 7111d99813e9418da8cfa0a73231e38e
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 19:24:09 EEST 2018

  def getGetViewHttpRouteName(): ViewHttpRouteName =
    ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

}
