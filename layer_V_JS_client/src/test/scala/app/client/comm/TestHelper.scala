package app.client.comm

object TestHelper {

}

//import app.client.rest.commands.forTesting.Helpers
import app.shared.rest.TestURLs
import app.testHelpersShared.data.TestDataLabels
import app.testHelpersShared.data.TestDataLabels.TestDataLabel
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
    //    val json="bla"
    println( "put test payload:" + json )
    val headers = Map( "Content-Type" -> "application/json" )
    //    val url: String = ResetURL().clientEndpointWithHost.asString

    val url: String = "/resetState"

    println( s"resetServer + url:${url}" )
    val f: Future[XMLHttpRequest] =
      Ajax.post( url, json, headers = headers )
    f
  }

  def resetServerToLabelOne()=resetServer(TestDataLabels.LabelOne)
  def resetServerToLabelThree()=resetServer(TestDataLabels.LabelThree)
}


trait BeforeTester {
  self: AsyncFunSuite =>

  def testWithBefore(before: => Future[Unit] )(description: String )(testToRun: => Future[Assertion] ): Unit = {
    test( description ) { before.flatMap( _ => testToRun ) }
  }

  def resetDBBeforeTest: Future[Unit] =
    Helpers.resetServer( TestDataLabels.LabelOne ).map( x => () )
}

