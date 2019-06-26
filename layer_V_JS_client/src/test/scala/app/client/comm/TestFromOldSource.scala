package app.client.comm

// copy pasted from :
// [https://github.com/jhegedus42/IM_shared_2018_11_22/blob/c9487cf220e6388cb4315d10e7cc2282c9a4a725/_archive/old_client_tests/test/scala/app/client/rest/view/SumIntViewTest.scala](https://github.com/jhegedus42/IM_shared_2018_11_22/blob/c9487cf220e6388cb4315d10e7cc2282c9a4a725/_archive/old_client_tests/test/scala/app/client/rest/view/SumIntViewTest.scala)

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.{SumIntView, SumIntView_Par, SumIntView_Res}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax
import org.scalatest.{Assertion, AsyncFunSuite, Matchers}

import scala.concurrent.Future
import scala.reflect.ClassTag
//import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

/**
  * Created by joco on 16/12/2017.
  */

class SumIntViewTest extends AsyncFunSuite with Matchers with BeforeTester {

  implicit override def executionContext =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  // ^^^^^ https://github.com/scalatest/scalatest/issues/1039


  // AEDBEDB3-D7FE-46F5-AFCA-9AE31BA820E7
  def postViewRequest[V <: View](
                                  requestParams: V#Par
                                )(
                                  implicit
                                  ct: ClassTag[V],
                                  encoder: Encoder[V#Par],
                                  decoder: Decoder[V#Res]
                                ): Future[V#Res] = {

    val routeName: ViewHttpRouteName = ViewHttpRouteNameProvider.getViewHttpRouteName[V]()
    val url: String = routeName.name

    val json_line: String = requestParams.asJson.spaces2 // encode
    val headers: Map[String, String] = Map("Content-Type" -> "application/json")

    Ajax
      .post(url, json_line, headers = headers)
      .map(_.responseText)
      .map((x: String) => {
        decode[V#Res](x)
      })
      .map(x => x.right.get)
  }

  testWithBefore(resetDBBeforeTest_StateLabelOne)(
    "sum int view should add two numbers correctly ") {

    def res: Future[Assertion] = {

      val parametersForViewQueryToBeSent = SumIntView_Par(2, 3)

      val resultOfViewQuery: Future[SumIntView_Res] =
        postViewRequest[SumIntView](parametersForViewQueryToBeSent)


      val x: Future[Assertion] = resultOfViewQuery.map({
        sumIntView_Res => {
          val assertionResult: Assertion =
            sumIntView_Res shouldBe SumIntView_Res(5)

          println(
            s"The result of the Future for the View Query $parametersForViewQueryToBeSent" +
            s"\n is : $sumIntView_Res"
          )

          assertionResult
        }

      })
      x
    }

    res
  }

}