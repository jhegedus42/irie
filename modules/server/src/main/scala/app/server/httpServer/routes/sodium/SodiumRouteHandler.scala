package app.server.httpServer.routes.sodium

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.RouteName
import app.shared.comm.postRequests.marshall.{JSONEncodersDecoders, ParametersAsJSON, ResultOptionAsJSON}
import app.shared.comm.sodium.{SodiumCRUDReq, SodiumRouteName}
import app.shared.entity.entityValue.EntityType

import scala.concurrent.Future
import io.circe.parser._
import io.circe.{Decoder, Error, _}
import io.circe.parser._
import io.circe.{Decoder, Encoder, Error, _}
import org.bouncycastle.ocsp.Req

trait SodiumCRUDRouteType

trait SodiumCRUDLogic[RT<:SodiumCRUDRouteType,E<:EntityType[E]]{
  def getLogic(paw:PersistentActorWhisperer) : String=>String
}

sealed trait SodiumCRUDRoute[RT<:SodiumCRUDRouteType,E<:EntityType[E]]{
  self: SodiumCRUDLogic[RT,E] =>

  def getName:String
  def getPaw:PersistentActorWhisperer
  def logic = getLogic(getPaw)
  def getRoute: Route =
    post {
      path(getName) {
        entity(as[String]) { s: String =>
          complete(logic(s))
        }
      }
    }

}


