package app.client.ui.caching

import app.shared.comm.GetEntityURLs
import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.refs.{TypedRef, TypedRefVal}
import io.circe
import io.circe.Decoder
import io.circe.parser.decode
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag



private[caching] object REST_ForEntity {

  def getEntity[E <: Entity[E]: ClassTag](
      ref:        TypedRef[E]
    )(implicit d: Decoder[TypedRefVal[E]]
    ): Future[TypedRefVal[E]] = {

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val route: String = GetEntityURLs.queryURL( ref )

    println( s"getEntity before creating future for $ref" )

    def dd(s: String ): Either[circe.Error, TypedRefVal[E]] = {
      println( s"getEntity: responseText $s" )
      decode( s )
    }

    val res: Future[TypedRefVal[E]] = Ajax
      .get( route )
      .map( _.responseText )
      .map( x => dd( x ) )
      .map(
        { x: Either[circe.Error, TypedRefVal[E]] =>
          {
            println( s"returned RefVal is $x" )
            x.right.get
          }
        }
      )
    res
  }

}
