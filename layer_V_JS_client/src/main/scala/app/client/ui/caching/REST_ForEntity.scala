package app.client.ui.caching

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}
import app.shared.rest.routes.crudRequests.GetEntityRequest
import io.circe
import io.circe.Decoder
import io.circe.parser.decode
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag



private[caching] object REST_ForEntity {

  def getEntity[E <: Entity: ClassTag](
      ref:        TypedRef[E]
    )(implicit d: Decoder[RefVal[E]]
    ): Future[RefVal[E]] = {

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val route: String = GetEntityRequest.queryURL( ref )

    println( s"getEntity before creating future for $ref" )

    def dd(s: String ): Either[circe.Error, RefVal[E]] = {
      println( s"getEntity: responseText $s" )
      decode( s )
    }

    val res: Future[RefVal[E]] = Ajax
      .get( route )
      .map( _.responseText )
      .map( x => dd( x ) )
      .map(
        { x: Either[circe.Error, RefVal[E]] =>
          {
            println( s"returned RefVal is $x" )
            x.right.get
          }
        }
      )
    res
  }

}
