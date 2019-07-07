package app.client.ui.caching

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}
import app.shared.rest.routes.crudRequests.GetEntityRequest
//import app.shared.rest.routes.crudRequests.GetEntityRequest.GetEntityReqResult
import io.circe
import io.circe.Decoder
import io.circe.parser.decode
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag

private[caching] object REST {

  // B40E8B54-85FC-4815-8281-B60C3E9D1B3F
  def getEntity[E <: Entity : ClassTag ](ref: TypedRef[E])(implicit d:Decoder[RefVal[E]]): Future[RefVal[E]] = {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    val route: String =GetEntityRequest.queryURL(ref)

//    val route: String = ???

    println(s"getEntity before creating future for $ref")

    def dd(s:String) : Either[circe.Error, RefVal[E]]= {
      println(s"getEntity: responseText $s")
      decode(s)
    }

    val res: Future[RefVal[E]] = Ajax
                                 .get( route )
                                 .map( _.responseText )
                                 .map( x=>dd(x) )
                                 .map(
                                       {
                                         x: Either[circe.Error, RefVal[E]] => {
                                           println(s"returned RefVal is $x")
                                           x.right.get
                                         }
                                       }
                                     )
    res
  }

// this is good for testing
//  def resetServer(label: TestDataLabel ): Future[XMLHttpRequest] = {
//    //    import io.circe.generic.auto._
//    //    import io.circe.syntax._
//
//    val json: String = TestDataLabels.toJSON(label).spaces2
//    //    val json="bla"
//    println( "put test payload:" + json )
//    val headers = Map( "Content-Type" -> "application/json" )
//    //    val url: String = ResetURL().clientEndpointWithHost.asString
//
//    val url: String = "/resetState"
//
//    println( s"resetServer + url:${url}" )
//    val f: Future[XMLHttpRequest] =
//      Ajax.post( url, json, headers = headers )
//    f
//  }
//

//  def resetServerToLabelOne(): Future[XMLHttpRequest] = resetServer(TestDataLabels.LabelOne)
//  def resetServerToLabelThree()=resetServer(TestDataLabels.LabelThree)

}

