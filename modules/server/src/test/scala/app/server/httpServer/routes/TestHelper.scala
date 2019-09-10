package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMessage, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestDataProvider
import app.shared.comm.{PostRequest, RouteName}
import app.shared.comm.postRequests.{
  GetEntityReq,
  InsertReq,
  ResetRequest,
  UpdateReq
}
import app.shared.comm.postRequests.InsertReq.InsertReqRes
import app.shared.comm.postRequests.marshall.EncodersDecoders._
import app.shared.comm.postRequests.marshall.{
  EncodersDecoders,
  ParametersAsJSON,
  ResultOptionAsJSON
}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import app.shared.comm.postRequests.GetEntityReq.{
  GetEntityReqPar,
  GetEntityReqRes
}
import app.shared.comm.postRequests.marshall.EncodersDecoders.{
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
  InsertReq,
  UpdateReq
}
import app.shared.comm.{PostRequest, RouteName}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
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
    user:              Entity[User],
    newFavoriteNumber: Int
  ): Unit = {

    val updatedUserValue =
      user.entityValue.lens(_.favoriteNumber).set(newFavoriteNumber)

    val updateRes: UpdateReq.UpdateReqRes[User] =
      getPostRequestResult[UpdateReq[User], User](
        UpdateReq.UpdateReqPar(
          user,
          updatedUserValue
        )
      )

  }

  def assertUserFavoriteNumber(
    user:           Entity[User],
    favoriteNumber: Int
  ): Unit = {

    val resFromServer: Entity[User] = getLatestEntity(
      user.refToEntity.stripVersion()
    )

    assert(
      resFromServer.entityValue.favoriteNumber === favoriteNumber
    )

    resFromServer.entityValue.favoriteNumber shouldNot equal(
      favoriteNumber + 1
    )

  }

  def resetServerState(): Unit = {
    case class DummyVal(s: String) extends EntityValue[DummyVal]
    val resetRes1: ResetRequest.Res =
      getPostRequestResult[ResetRequest, DummyVal](
        ResetRequest.Par()
      )
  }

  def getPostRequestResult[
    Req <: PostRequest,
    V <: EntityValue[
      V
    ]
  ](par: Req#Par
  )(
    implicit
    encoder: Encoder[Req#Res],
    decoder: Decoder[Req#Res],
    enc_par: Encoder[Req#Par],
    e2:      Encoder[Entity[V]],
    ct1:     ClassTag[Req#PayLoad],
    ct2:     ClassTag[Req]
  ): Req#Res = {

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

    val res: Req#Res =
      decodeResult[Req](
        ResultOptionAsJSON(resp)
      ).toOption.get

    res

  }

  // todo-later guzsba kotni az impliciteket itt, felhasznalva a Macska pattern-t

  def executeUpdateUserRequest(
    currentEntity: Entity[User],
    newValue:      User
  ): Entity[User] = {

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

  def executeInsertUserRequest(u: User): Entity[User] = {

    val rn: String = "/" + RouteName
      .getRouteName[InsertReq[User]]()
      .name

    val mhb: User = u

    val par: InsertReq.InsertReqPar[User] =
      InsertReq.InsertReqPar(mhb)

    val json: ParametersAsJSON =
      encodeParameters[InsertReq[User]](par)

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

    val ent: Entity[User] =
      decodeResult[InsertReq[User]](
        ResultOptionAsJSON(resp)
      ).right.get.entity

    ent

  }

  def getLatestEntity[V <: EntityValue[V]](
    ref: RefToEntityWithoutVersion[V]
  )(
    implicit
    encoder: Encoder[GetEntityReq[V]#Res],
    decoder: Decoder[GetEntityReq[V]#Res],
    enc_ent: Encoder[Entity[V]],
    ct1:     ClassTag[GetEntityReq[V]#PayLoad]
  ): Entity[V] = {

    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val par: GetEntityReqPar[V] =
      GetEntityReqPar(ref)

    val res: GetEntityReqRes[V] =
      getPostRequestResult[GetEntityReq[V], V](par)

    val entity = res.optionEntity.get
    entity
  }

  def assertLatestEntityValueIs[V <: EntityValue[V]](
    ref: RefToEntityWithoutVersion[V],
    ev:  EntityValue[V]
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
  def assertLatestEntityIs(entity: Entity[User]): Unit = {
    val rn: String = "/" + RouteName
      .getRouteName[GetEntityReq[User]]()
      .name

    val req = Post(rn).withEntity(
      encodeParameters[GetEntityReq[User]](
        GetEntityReqPar(entity.refToEntity.stripVersion())
      ).parameters_as_json
    )

    val expectedResponse: String = {
      val r: Option[GetEntityReqRes[User]] = Some(
        GetEntityReqRes(Some(entity))
      )
      encodeResult[GetEntityReq[User]](r).resultOptionAsJSON
    }

    req ~> routes.route ~> check {
      responseAs[String] shouldEqual expectedResponse
    }

  }
}
