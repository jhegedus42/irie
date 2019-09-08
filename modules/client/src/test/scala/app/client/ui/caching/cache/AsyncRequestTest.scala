package app.client.ui.caching.cache

import app.client.ui.caching.cache.AJAXCalls.{AjaxCallPar, PostAJAXRequestSuccessfulResponse}
import app.shared.comm.postRequests.{GetEntityReq, InsertReq, UpdateReq}
import app.shared.comm.postRequests.GetEntityReq.GetEntityReqPar
import app.shared.comm.postRequests.InsertReq.InsertReqPar
import app.shared.comm.postRequests.UpdateReq.UpdateReqPar
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.{Assertion, AsyncFunSuite}
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContextExecutor, Future}

class AsyncRequestTest extends AsyncFunSuite {
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

    val ajaxCallPar = AjaxCallPar[GetEntityReq[User]]( requestPar )

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
      AJAXCalls.PostAJAXRequestSuccessfulResponse[GetEntityReq[User]]
    ] =
      AJAXCalls.sendPostAjaxRequest( ajaxCallPar )
    res.map( _.res.optionEntity.get )
  }

  test( "get entity test" ) {
    val alice: Entity[User] = TestUsers.aliceEntity_with_UUID0

    getUser( alice ).map(
      (u: Entity[User]) => assertUserNamesAreEqual( u, alice.entityValue )
    )
  }

  test( "insert and then get" ) {
    val c   = TestUsers.cica

    val par = AjaxCallPar[InsertReq[User]]( InsertReqPar( c ) )

    val ac
      : Future[PostAJAXRequestSuccessfulResponse[InsertReq[User]]] =
      AJAXCalls.sendPostAjaxRequest( par )

    val insertResult: Future[Entity[User]] = ac.map( _.res.entity )

    val getResult: Future[Entity[User]] =
      insertResult.flatMap( (e: Entity[User]) => getUser( e ) )


    val futureAssertionThatEverythingIsKosher: Future[Assertion] =
      getResult.map((e: Entity[User]) => assertUserNamesAreEqual( e, c ) )
    futureAssertionThatEverythingIsKosher
  }

  test( "update and then get" ) {
    val ent   = TestUsers.meresiHiba_with_UUID2
    import monocle.macros.syntax.lens._

    val e2: Entity[User] = ent.lens(_.entityValue.favoriteNumber).set(66)

    val e2u: User =e2.entityValue


    val p: UpdateReqPar[User] = UpdateReqPar[User]( ent,e2u )
    val par = AjaxCallPar[UpdateReq[User]](p )

    val ac
    : Future[PostAJAXRequestSuccessfulResponse[UpdateReq[User]]] =
      AJAXCalls.sendPostAjaxRequest( par )

    val res: Future[Entity[User]] = ac.map( _.res.entity )



    val res2: Future[Entity[User]] =res.flatMap(e=>getUser( e ))

    res2.onComplete(x=>println(s"getUser in update test completed with :\n$x"))

    val to_return=   for {
        r1<-res
        r2<-res2
        a=r2.entityValue.favoriteNumber==66
        b=r1.entityValue.favoriteNumber==66
        c=r2.refToEntity.entityVersion.versionNumberLong==1
      } yield (assert(a&&b&&c))
    to_return
  }
}
