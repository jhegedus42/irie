package app.client.ui.caching.cache

import app.client.ui.caching.cache.AJAXCalls.{AjaxCallPar, PostAJAXRequestSuccessfulResponse}
import app.shared.comm.postRequests.SumIntRoute
import app.shared.comm.postRequests.SumIntRoute.{SumIntPar, SumIntRes}
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.{ExecutionContextExecutor, Future}
import io.circe.generic.auto._

class SumIntRequestAsyncTest extends AsyncFunSuite {
  override implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  test( "simple - 1==1 in the Future - async test" ) {
    Future( assert( 1 == 1 ) )
  }

  test( "simple async sum request test" ) {

    val par: SumIntPar = SumIntPar( 4, 5 )
    val apar: AjaxCallPar[SumIntRoute] =
      AjaxCallPar[SumIntRoute]( par )

    val expectedResult: SumIntRes = SumIntRes( 9 )

    val expectedCallResult: PostAJAXRequestSuccessfulResponse[SumIntRoute] =
      PostAJAXRequestSuccessfulResponse[SumIntRoute]( par, expectedResult )

    val callResult: Future[PostAJAXRequestSuccessfulResponse[SumIntRoute]] =
      AJAXCalls.sendPostAjaxRequest( apar )

    val resInt: Future[Int] =
      callResult.map( decodingSuccess => decodingSuccess.res.sum )

    val res: Future[Assertion] = resInt.map( int => {
      assert( (int === 9) && (int !== 10) )
    } )

    res

  }

}
