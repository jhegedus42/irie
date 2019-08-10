package app.server.httpServer.routes.view

import app.shared.comm.views.marshall.{JSONContainingGetViewPar, JSONContainingOptRes}
import app.shared.comm.views.{View, ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.server.logic.ServerSideLogic.ServerLogicTypeClass
import app.shared.comm.views.marshall.ViewCirceEncodingDecoding._
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder, Error}

import scala.reflect.ClassTag




case class ViewRequestHandler[V1 <: View: ClassTag]() {

  def decodeJSON2Par_SendParToLogic_EncodeResultToJSON(
      paramJSON: JSONContainingGetViewPar
    )(
      implicit
      decoder:     Decoder[V1#Par],
      encoder:     Encoder[V1#Res],
      serverLogic: ServerLogicTypeClass[V1]
    ): JSONContainingOptRes = {


    val r: Either[Error, V1#Par] = decodeJSONToPar[V1]( paramJSON )

    val parOpt: Option[V1#Par] = r.right.toOption

    val resOpt: Option[V1#Res] = for {
      par <- parOpt
      res <- serverLogic.getView( par )
    } yield res


    (encodeOptResToJSONContainingOptRes[V1]( resOpt ) )

  }


  def getGetViewHttpRouteName: ViewHttpRouteName =
    ViewHttpRouteNameProvider.getViewHttpRouteName[V1]

}
