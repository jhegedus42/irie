package app.client.ui.caching.cache

import app.client.ui.caching.cache.AJAXCalls.{
  AjaxCallPar,
  PostAJAXRequestSuccessfulResponse
}
import app.shared.comm.postRequests.{GetEntityRoute, InsertNewEntityRoute}
import app.shared.comm.postRequests.GetEntityRoute.GetEntityReqPar
import app.shared.comm.postRequests.InsertNewEntityRoute.InsertReqPar
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.{Assertion, AsyncFunSuite}
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContextExecutor, Future}

class GetEntityAsyncRequestTest extends AsyncFunSuite {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def assertUserNamesAreEqual(
      resultingEntity: Entity[User],
      user:            User
  ): Assertion = {

    println( s"""
         |
         |---------------------------------------------
         |
         |GetEntityAsyncRequestTest - returned entity:
         |
         |
         |$resultingEntity
         |
         |
         |
         |Expected entity :
         |
         |$user
         |
         |
         |
         |---------------------------------------------
         |
       """.stripMargin )

    assert( resultingEntity.entityValue.name === user.name )
  }

  def getUser( entity: Entity[User] ): Future[Entity[User]] = {

    val requestPar: GetEntityReqPar[User] =
      GetEntityReqPar( entity.refToEntity.stripVersion() )

    val ajaxCallPar = AjaxCallPar[GetEntityRoute[User]]( requestPar )

    println( s"""
         |vvvvvvvvvvvvvvvvvvvvvvvvvvvvv
         |
        |val ajaxCallPar = AjaxCallPar[GetEntityPostRequest[User]]( requestPar )
         |
        |is
         |
        |$ajaxCallPar
         |
        |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      """.stripMargin )

    val res: Future[
      AJAXCalls.PostAJAXRequestSuccessfulResponse[GetEntityRoute[User]]
    ] =
      AJAXCalls.sendPostAjaxRequest( ajaxCallPar )
    res.map( _.res.optionEntity.get )
  }

  test( "get entity test" ) {
    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0

    getUser( alice ).map(
      (u: Entity[User]) => assertUserNamesAreEqual( u, alice.entityValue )
    )
  } //todo-now => make this pass

  test( "insert and then get" ) {
    val c   = TestUsers.cica

    val par = AjaxCallPar[InsertNewEntityRoute[User]]( InsertReqPar( c ) )

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[InsertNewEntityRoute[User]]] =
      AJAXCalls.sendPostAjaxRequest( par )

    val insertResult: Future[Entity[User]] = ac.map( _.res.entity )

    val getResult: Future[Entity[User]] =
      insertResult.flatMap( (e: Entity[User]) => getUser( e ) )


    val futureAssertionThatEverythingIsKosher: Future[Assertion] =
      getResult.map( e => assertUserNamesAreEqual( e, c ) )
    futureAssertionThatEverythingIsKosher
  }

}
