package app.client.comm

// copy pasted from :
// [https://github.com/jhegedus42/IM_shared_2018_11_22/blob/c9487cf220e6388cb4315d10e7cc2282c9a4a725/_archive/old_client_tests/test/scala/app/client/rest/view/SumIntViewTest.scala](https://github.com/jhegedus42/IM_shared_2018_11_22/blob/c9487cf220e6388cb4315d10e7cc2282c9a4a725/_archive/old_client_tests/test/scala/app/client/rest/view/SumIntViewTest.scala)

//import app.client.rest.commands.BeforeTester
//import app.client.rest.commands.forTesting.Helpers
//import app.client.rest.commands.generalCRUD.{CreateEntityAJAX, GetAllEntitiesAJAX}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.SomeError_Trait
import app.shared.data.model.Entity.Entity
//import app.shared.data.model.{DataType, LineText}
import app.shared.data.ref.RefVal
//import app.shared.rest.routes.crudRequests.CreateEntityRequest.CEC_Res
//import app.shared.rest.routes.crudRequests.{CreateEntityRequest, GetAllEntitiesRequest}
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.{SumIntView, SumIntView_Par, SumIntView_Res}
import app.testHelpersShared.data.TestDataLabels
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import org.scalatest.{Assertion, AsyncFunSuite, Matchers}
import scalaz.\/
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.generic.auto._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

import scala.concurrent.Future
import scala.reflect.ClassTag
//import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

/**
  * Created by joco on 16/12/2017.
  */
// f1781fa239f1469c83f096744c35349d$5e45b350c3d7df91abb31d34817ad48226d70ff8

class SumIntViewTest extends AsyncFunSuite with Matchers with BeforeTester {

  implicit override def executionContext =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  // ^^^^^ https://github.com/scalatest/scalatest/issues/1039

  def postViewRequest[V <: View](
                                  requestParams: V#Par
                                )(
                                  implicit
                                  ct:      ClassTag[V],
                                  encoder: Encoder[V#Par],
                                  decoder: Decoder[V#Res]
                                ): Future[V#Res] = {
    // 23c629d1c012400ea7b2133194a0ddfc$5e45b350c3d7df91abb31d34817ad48226d70ff8

    val routeName: ViewHttpRouteName = ViewHttpRouteNameProvider.getViewHttpRouteName[V]()
    val url:       String            = routeName.name

    val json_line: String              = requestParams.asJson.spaces2 // encode
    val headers:   Map[String, String] = Map( "Content-Type" -> "application/json" )

    Ajax
      .post( url, json_line, headers = headers ) // a3eeb6d8aa9a48cd8af81c686b7ba75c$5e45b350c3d7df91abb31d34817ad48226d70ff8
      .map( _.responseText )
      .map( (x: String) => { decode[V#Res]( x ) } )
      .map( x => x.right.get )
  }

  testWithBefore( resetDBBeforeTest )( "sum int view should add two numbers " ) {

    def res: Future[Assertion] ={

      val whatWeWantToSend = SumIntView_Par( 2, 3 )
      val res: Future[SumIntView_Res] = postViewRequest[SumIntView](whatWeWantToSend)

      // ??? // ff841b25d2cc47f7923626031f50ed73$5e45b350c3d7df91abb31d34817ad48226d70ff8

      val x: Future[Assertion] = res.map(res => res shouldBe SumIntView_Res(5))
      x
    }

    res
  }

}