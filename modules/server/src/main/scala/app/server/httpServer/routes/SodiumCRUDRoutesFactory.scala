package app.server.httpServer.routes

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import app.server.StateHolder
import dataModel.{EntityValueType, User}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import syncedNormalizedState.comm.{GetAllLatestEntities, _}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait StateHolderProvider {
  implicit def stateholder: StateHolder

  implicit def executionContext: ExecutionContextExecutor
}

trait SodiumCRUDRoute[
  RT <: SodiumCRUDReq[E],
  E  <: EntityValueType[E]] {
  self: SodiumParamConverters[RT, E] with StateHolderProvider =>

  import self.converters._

  implicit def stateholder:      StateHolder
  implicit def executionContext: ExecutionContextExecutor

  def getLogicS(par: String)(): Future[String] = {
    def f(x: RT#Resp): String = x
    stateholder.handleCDUDRequest[RT, E](par).map(f)
  }

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
