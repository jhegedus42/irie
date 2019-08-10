package app.client.comm

object TestHelper {

}

//import app.client.rest.commands.forTesting.Helpers
//import app.shared.rest.TestURLs
import app.shared.dataModel.entity.testData.{LabelOne, LabelThree, TestDataLabel, TestDataLabels}
//import com.sun.deploy.ref.Helpers
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.Future

object Helpers {

  def resetServer(label: TestDataLabel ): Future[XMLHttpRequest] = {
    //    import io.circe.generic.auto._
    //    import io.circe.syntax._

    val json: String = TestDataLabels.toJSON(label).spaces2
    println(s"we reset the server to the state described by the TestDataLabel : $label")
    //    val json="bla"
    println( "app.client.comm.Helpers.resetServer's POST payload:" + json )
    val headers = Map( "Content-Type" -> "application/json" )
    //    val url: String = ResetURL().clientEndpointWithHost.asString

    val url: String = "/resetState"

    println( s"app.client.comm.Helpers.resetServer's url:${url}" )
    val f: Future[XMLHttpRequest] =
      Ajax.post( url, json, headers = headers )
    f
  }

  def resetServerToLabelOne()=resetServer(LabelOne)
  def resetServerToLabelThree()=resetServer(LabelThree)
}


trait BeforeTester {
  self: AsyncFunSuite =>

  def testWithBefore(before: => Future[Unit] )(description: String )(testToRun: => Future[Assertion] ): Unit = {
    test( description ) { before.flatMap( _ => testToRun ) }
  }

  def resetDBBeforeTest_StateLabelOne: Future[Unit] =
    Helpers.resetServer( LabelOne ).map( x => () )
}

