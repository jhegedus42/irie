package app.___comm_model_on_the_server_side.serverSide.akkaHttpWebServer
//  import akka.http.scaladsl.server.directives.MethodDirectives.get
//  import akka.http.scaladsl.server.directives.ParameterDirectives.parameters
//  import akka.http.scaladsl.server.directives.PathDirectives.path
import app.___comm_model_on_the_server_side.serverSide.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.comm.views.CirceUtils._
import app.shared.comm.views.{JSONContainingGetViewPar, JSONContainingOptRes, ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.shared.data.model.View
import app.shared.data.model.View1_HolderObject.View1
import app.shared.data.model.View2_HolderObject.View2
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder, Error}

import scala.reflect.ClassTag

case class JSON(string: String )

case class HttpServerOnTheInternet() {

  val view1_getViewRequestHandler: GetViewRequestHandler[View1] =
    new GetViewRequestHandler[View1]()

  val view2_getViewRequestHandler: GetViewRequestHandler[View2] =
    new GetViewRequestHandler[View2]()

  val view1_routeName: ViewHttpRouteName =
    view1_getViewRequestHandler.getGetViewHttpRouteName

  val view2_routeName: ViewHttpRouteName =
    view2_getViewRequestHandler.getGetViewHttpRouteName

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
            .decodeJSON2Par_SendParToLogic_EncodeResultToJSON( requestPayload )
        )
      }

      case view2_routeName.`name` =>
        Some(
          view1_getViewRequestHandler
            .decodeJSON2Par_SendParToLogic_EncodeResultToJSON( requestPayload )
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

case class GetViewRequestHandler[V_method <: View:ClassTag ]() {

  def decodeJSON2Par_SendParToLogic_EncodeResultToJSON(
      paramJSON: JSONContainingGetViewPar
    )(
      implicit
      decoder:     Decoder[V_method#Par],
      encoder:     Encoder[V_method#Res],
      serverLogic: ServerLogicTypeClass[V_method]
    ): JSONContainingOptRes = {

//    println("GetViewRequestHandler's decodeJSON2Par_SendParToLogic_EncodeResultToJSON is called " +
//            "for the " + getGetViewHttpRouteName() + " route, with parameter: "+paramJSON)

    val r: Either[Error, V_method#Par] = decodeJSONToPar[V_method]( paramJSON )

    val parOpt: Option[V_method#Par] = r.right.toOption

    val resOpt: Option[V_method#Res] = for {
      par <- parOpt
      res <- serverLogic.getView( par )
    } yield res

//    println("it's results is (before encoding it):"+resOpt)

    (encodeOptResToJSONContainingOptRes[V_method]( resOpt ) )

  }

  // Random UUID: 7111d99813e9418da8cfa0a73231e38e
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 19:24:09 EEST 2018

  def getGetViewHttpRouteName: ViewHttpRouteName =
    ViewHttpRouteNameProvider.getViewHttpRouteName[V_method]

}
