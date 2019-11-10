package _notRelevant.caching.cache.sodiumCache

import dataModel.EntityValueType
import syncedNormalizedState.comm.{ClassTagPrivoders, SodiumCRUDReq, SodiumParamConverters, SodiumRouteNameProvider}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.{ExecutionContextExecutor, Future}

trait AJAXModul[RT <: SodiumCRUDReq[V], V <: EntityValueType[V]] {
  self: SodiumParamConverters[RT, V] with ClassTagPrivoders[RT, V] =>

  import self.converters._
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

//  implicit def ct: ClassTag[RT]
//  implicit def e:  ClassTag[V]

  def url: String = SodiumRouteNameProvider.getRouteName[V, RT]().name

  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val ajaxCall: String => Future[String] = json_line =>
    Ajax
      .post(url, json_line, headers = headers)
      .map(_.responseText)

  def query(
    par: RT#Par
  )(
    implicit decoder: Decoder[RT#Resp],
    encoder:          Encoder[RT#Par]
  ): Future[RT#Resp] = ajaxCall(parToString(par)).map(stringToResp)

}
