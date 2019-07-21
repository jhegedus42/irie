package app.client.ui.caching

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}
import app.shared.rest.routes.crudRequests.GetEntityRequest
import io.circe.Encoder

import scala.concurrent.ExecutionContextExecutor
//import app.shared.rest.routes.crudRequests.GetEntityRequest.GetEntityReqResult
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag
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


  private[viewCache] def getView[V <: View](
                               requestParams: V#Par
                             )(implicit ct:   ClassTag[V],
                               encoder:       Encoder[V#Par],
                               decoder:       Decoder[V#Res]
                             ): Future[V#Res] = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    val routeName: ViewHttpRouteName =
      ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

    val url: String = routeName.name
    import io.circe.parser.decode
    import io.circe.syntax._

    val json_line: String = requestParams.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    val res: Future[V#Res] =
      Ajax
        .post( url, json_line, headers = headers )
        .map( _.responseText )
        .map( (x: String) => {
          decode[V#Res]( x )
        } )
        .map( x => x.right.get )

    res
  }
}

