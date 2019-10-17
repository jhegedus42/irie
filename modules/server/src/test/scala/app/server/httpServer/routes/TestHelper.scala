package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMessage, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
import app.shared.comm.{PostRequest, RouteName, WriteRequest}
import app.shared.comm.postRequests.CreateEntityReq.CreateEntityReqRes
import app.shared.comm.postRequests.marshall.JSONEncodersDecoders._
import app.shared.comm.postRequests.marshall.{
  JSONEncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.io.dns.DnsProtocol.RequestType
import app.shared.comm.postRequests.GetEntityReq.{Par, Res}
import app.shared.comm.postRequests.marshall.JSONEncodersDecoders.{
  decodeResult,
  encodeParameters,
  encodeResult
}
import app.shared.comm.postRequests.marshall.{
  ParametersAsJSON,
  ResultOptionAsJSON
}
import app.shared.comm.postRequests.{
  GetEntityReq,
  CreateEntityReq,
  ResetRequest,
  UpdateReq
}
import app.shared.comm.{PostRequest, RouteName}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.{RefToEntityWithVersion}
import io.circe
import io.circe.{Decoder, Encoder}
import org.scalatest.{FunSuite, Matchers}

import scala.reflect.ClassTag
import io.circe.generic.auto._
import monocle.macros.syntax.lens._

case class TestHelper(routes: RouteFactory)
    extends FunSuite
    with Matchers
    with ScalatestRouteTest {

  def updateUsersFavoriteNumer(
    user:              EntityWithRef[User],
    newFavoriteNumber: Int
  ): EntityWithRef[User] = {

    val updatedUserValue =
      user.entityValue.lens(_.favoriteNumber).set(newFavoriteNumber)

    val updateRes: UpdateReq.UpdateReqRes[User] =
      getPostRequestResult[UpdateReq[User] ](
        UpdateReq.UpdateReqPar(
          user,
          updatedUserValue
        )
      )

    val res=updateRes.entity
    res
  }

  def assertUserFavoriteNumber(
    user:           EntityWithRef[User],
    favoriteNumber: Int
  ): Unit = {

    val resFromServer: EntityWithRef[User] = getEntity(
      user.refToEntity
    )

    assert(
      resFromServer.entityValue.favoriteNumber === favoriteNumber
    )

    resFromServer.entityValue.favoriteNumber shouldNot equal(
      favoriteNumber + 1
    )

  }

  def resetServerState(): Unit = {
    case class DummyVal(s: String) extends EntityType[DummyVal]
    val resetRes1: ResetRequest.Res =
      getPostRequestResult[ResetRequest ](
        ResetRequest.Par()
      )
  }

  def getPostRequestResult[
    Req <: PostRequest[_]
//    V   <: EntityType[V]
  ](par: Req#ParT
  )(
    implicit
    encoder: Encoder[Req#ResT],
    decoder: Decoder[Req#ResT],
    enc_par: Encoder[Req#ParT],
//    e2:      Encoder[EntityWithRef[V]],
    ct1:     ClassTag[Req#PayLoadT],
    ct2:     ClassTag[Req]
  ): Req#ResT = {

    val rn: String = "/" + RouteName
      .getRouteName[Req]()
      .name

    val encodedPars: ParametersAsJSON = encodeParameters(par)

    val req = Post(rn).withEntity(
      encodedPars.parameters_as_json
    )

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val res: Req#ResT =
      decodeResult[Req](
        ResultOptionAsJSON(resp)
      ).toOption.get

    res

  }

  // todo-later guzsba kotni az impliciteket itt, felhasznalva a Macska pattern-t

  def executeUpdateUserRequest(
    currentEntity: EntityWithRef[User],
    newValue:      User
  ): EntityWithRef[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[UpdateReq[User]]()
      .name

    val par: UpdateReq.UpdateReqPar[User] =
      UpdateReq
        .UpdateReqPar[User](currentEntity, newValue)

    val json: ParametersAsJSON =
      encodeParameters[UpdateReq[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val resDecoded: Either[
      circe.Error,
      UpdateReq.UpdateReqRes[User]
    ] =
      decodeResult[UpdateReq[User]](
        ResultOptionAsJSON(resp)
      )

    val resUnsafeExtracted: UpdateReq.UpdateReqRes[User] =
      resDecoded.right.get

    val returnedEntity = resUnsafeExtracted.entity

    returnedEntity

  }

  def executeInsertUserRequest(u: User): EntityWithRef[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[CreateEntityReq[User]]()
      .name

    val mhb: User = u

    val par: CreateEntityReq.CreateEntityReqPar[User] =
      CreateEntityReq.CreateEntityReqPar(mhb)

    val json: ParametersAsJSON =
      encodeParameters[CreateEntityReq[User]](par)

    val json_par_as_string: String = json.parameters_as_json

    val req = Post(rn).withEntity(json_par_as_string)

    //    req

    var resp: String = null

    req ~> routes.route ~> check {
      val r = responseAs[String]
      resp = r
      true
    }

    println(resp)

    val ent: EntityWithRef[User] =
      decodeResult[CreateEntityReq[User]](
        ResultOptionAsJSON(resp)
      ).right.get.entity

    ent

  }

  def getEntity[V <: EntityType[V]](
    ref: RefToEntityWithVersion[V]
  )(
    implicit
    encoder: Encoder[GetEntityReq[V]#ResT],
    decoder: Decoder[GetEntityReq[V]#ResT],
    enc_ent: Encoder[EntityWithRef[V]],
    ct1:     ClassTag[GetEntityReq[V]#PayLoadT]
  ): EntityWithRef[V] = {

    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val par: Par[V] =
      Par(ref)

    val res: Res[V] =
      getPostRequestResult[GetEntityReq[V] ](par)

    val entity = res.optionEntity.get
    entity
  }

  def assertLatestEntityValueIs[V <: EntityType[V]](
    ref: RefToEntityWithVersion[V],
    ev:  EntityType[V]
  ): Unit = {
    ??? // todo-later maybe

  }

  /**
    * Assert that given entity is in the servers database and
    * the newest version that the server has is the same
    * that we have.
    *
    * @param entity
    */
  def assertLatestEntityIs(entity: EntityWithRef[User]): Unit = {
    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val req = Post(rn).withEntity(
      encodeParameters[GetEntityReq[User]](
        Par(entity.refToEntity)
      ).parameters_as_json
    )

    val expectedResponse: String = {
      val r: Option[Res[User]] = Some(
        Res(Some(entity))
      )
      encodeResult[GetEntityReq[User]](r).resultOptionAsJSON
    }

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expectedResponse
    }

  }
}
