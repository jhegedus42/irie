package app.server.httpServer.routes.sodium

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.persistentActor.PersistentActorWhisperer
import dataModel.User
import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{EntityType, EntityWithRef}
import sodiumComm._

import scala.concurrent.Future

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

import scala.concurrent.Future

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
