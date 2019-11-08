package app.server.httpServer.routes.sodium

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.RouteName
import app.shared.comm.postRequests.marshall.{JSONEncodersDecoders, ParametersAsJSON, ResultOptionAsJSON}
import app.shared.comm.sodium.{ClassTagPrivoders, SodiumCRUDReq, SodiumParamConverterImpl, SodiumRouteName, SodiumRouteNameProvider}
import app.shared.entity.entityValue.EntityType

import scala.reflect.ClassTag
import scala.concurrent.Future
import io.circe.parser._
import io.circe.{Decoder, Error, _}
import io.circe.parser._
import io.circe.{Decoder, Encoder, Error, _}
import org.bouncycastle.ocsp.Req

trait SodiumCRUDLogic[RT <: SodiumCRUDReq[E], E <: EntityType[E]] {
  self: SodiumParamConverterImpl[RT,E]=>
  def getLogic(paw: PersistentActorWhisperer): RT#Par => RT#Resp

  def getLogicS( implicit paw: PersistentActorWhisperer): String => String = {
    par => getLogic(paw)(par)
  }
}

sealed trait SodiumCRUDRoute[
  RT <: SodiumCRUDReq[E],
  E  <: EntityType[E]] {
  self: SodiumCRUDLogic[RT, E] with ClassTagPrivoders[RT,E]  =>

//  implicit def ct:ClassTag[RT]
//  implicit def e:ClassTag[E]

  def url: String = SodiumRouteNameProvider.getRouteName[E,RT]().name
  def getPaw:  PersistentActorWhisperer
  def logic = getLogicS(getPaw)

  def getRoute: Route =
    post {
      path(url) {
        entity(as[String]) { s: String =>
          complete(logic(s))
        }
      }
    }

}
