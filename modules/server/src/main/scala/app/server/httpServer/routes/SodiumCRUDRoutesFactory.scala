package app.server.httpServer.routes

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import app.server.httpServer.StateHolder
import comm.crudRequests.{GetAllLatestEntities, CRUDReq}
import dataStorage.normalizedDataModel.{EntityValueType, User}
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic._
import io.circe.generic.JsonCodec
import comm.{GetAllLatestEntities, _}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

trait StateHolderProvider {
  implicit def stateholder: StateHolder

  implicit def executionContext: ExecutionContextExecutor
}

trait SodiumCRUDRoute[
  RT <: CRUDReq[E],
  E  <: EntityValueType[E]] {
  self: SodiumParamConverters[RT, E]
    with StateHolderProvider
    with ClassTagPrivoders[RT, E] =>

  import io.circe.syntax._
  import io.circe.generic.auto._
  import io.circe.generic.JsonCodec


  implicit def stateholder:      StateHolder
  implicit def executionContext: ExecutionContextExecutor

//  val imp1=implicitly[Encoder[RT#Resp]]
//  val imp2=implicitly[Encoder[RT#Resp]]

  def getLogicS(par: String): Future[String] = {
    def f(x: RT#Resp): String = converters.respToString(x)
    val p=converters.stringToPar(par)
    stateholder.handleCDUDRequest[RT, E](p).map(f(_))
  }

  implicit def ct: ClassTag[RT]

  implicit def e: ClassTag[E]

  def url: String = SodiumRouteNameProvider.getRouteName[E, RT]().name

  def getRoute: Route =
    post {
      path(url) {
        entity(as[String]) { s: String =>
          complete(getLogicS(s))
        }
      }
    }

}

import scala.concurrent.Future

case class CRUDRouteFactory(
  implicit stateHolder:     StateHolder,
  executionContextExecutor: ExecutionContextExecutor) {

  trait Implicits extends StateHolderProvider {
    implicit def stateholder: StateHolder = stateHolder

    implicit def executionContext: ExecutionContextExecutor =
      executionContextExecutor
  }

  type VV   = User
  type ReqQ = GetAllLatestEntities[VV]

  case class GetAllUsersSodiumRoute()()
      extends ClassTagPrivoders[ReqQ, VV]
      with SodiumCRUDRoute[ReqQ, VV]
      with SodiumParamConverters[ReqQ, VV]
      with Implicits {}
}
