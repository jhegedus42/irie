package app.server.httpServer.routes.sodium

import akka.http.scaladsl.server.Directives.{
  as,
  complete,
  entity,
  path,
  post
}
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.server.httpServer.routes.sodium.SodiumExampleRoutes.{
  ReqQ,
  VV
}
import app.shared.comm.RouteName
import app.shared.comm.postRequests.marshall.{
  JSONEncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import app.shared.comm.sodium.{
  ClassTagPrivoders,
  GetAllLatestEntities,
  SodiumCRUDReq,
  SodiumParamConverters,
  SodiumRouteName,
  SodiumRouteNameProvider
}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User

import scala.reflect.ClassTag
import scala.concurrent.Future
import io.circe.parser._
import io.circe.{Decoder, Error, _}
import io.circe.parser._
import io.circe.{Decoder, Encoder, Error, _}
import org.bouncycastle.ocsp.Req
import app.shared.comm.{PostRequest, PostRequestType}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.{Decoder, Error, _}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait SodiumCRUDLogic[RT <: SodiumCRUDReq[E], E <: EntityType[E]] {
  self: SodiumParamConverters[RT, E] =>

  def getLogic(paw: PersistentActorWhisperer): RT#Par => Future[RT#Resp]

  def getLogicS(
    paw: PersistentActorWhisperer
  )(
    implicit encoder2: Encoder[RT#Resp],
    encoder:           Encoder[RT#Par],
    decoder:           Decoder[RT#Par]
  ): String => Future[String] = { par: String =>
    implicit val ec=paw.getExecutionContext
    val res = getLogic(paw)(stringToPar(par))
    res.map(respToString(_))

  }
}

sealed trait SodiumCRUDRoute[
  RT <: SodiumCRUDReq[E],
  E  <: EntityType[E]] {
  self: SodiumCRUDLogic[RT, E] with ClassTagPrivoders[RT, E] =>

//  implicit def ct:ClassTag[RT]
//  implicit def e:ClassTag[E]

  def url: String = SodiumRouteNameProvider.getRouteName[E, RT]().name
  println(s"path for Sodium CRUD is :$url")
  def getPaw: PersistentActorWhisperer

  def logic(
    implicit
    e: Encoder[RT#Resp],
    d: Decoder[RT#Resp],

    e2: Encoder[RT#Par],
    d2: Decoder[RT#Par]
  ): String => Future[String] = getLogicS(getPaw)

  def getRoute(
    implicit
    e: Encoder[RT#Par],
    d: Decoder[RT#Par],
      e2: Encoder[RT#Resp],
  d2: Decoder[RT#Resp]
  ): Route =
    post {
      path(url) {
        entity(as[String]) { s: String =>
          val f: String => Future[String] = logic
          complete(f(s))
        }
      }
    }

}

import scala.concurrent.{ExecutionContextExecutor, Future}

object SodiumExampleRoutes {
  val getAllUsersRoute = ???

  type VV   = User
  type ReqQ = GetAllLatestEntities[VV]

  trait SodiumCRUDLogicImpl
      extends SodiumCRUDLogic[ReqQ, VV]
      with SodiumParamConverters[ReqQ, VV] {
    import GetAllLatestEntities._

    override def getLogic(
      paw: PersistentActorWhisperer
    ): Par => Future[Resp[VV]] = { _ =>
      val res: Future[Option[Set[EntityWithRef[VV]]]] = paw.getNewestEntitiesWithGivenEntityType[VV]

      implicit val e=paw.getExecutionContext
      res.map(Resp(_))
    }
  }

  case class GetAllUsersSodiumRoute(paw: PersistentActorWhisperer)
      extends ClassTagPrivoders[ReqQ, VV]
      with SodiumCRUDRoute[ReqQ, VV]
      with SodiumCRUDLogicImpl {
    override def getPaw: PersistentActorWhisperer = paw

  }
}