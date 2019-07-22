package app.client.ui.caching.viewCache
import app.client.ui.caching.CacheInterface
import app.client.ui.caching.REST.getEntity
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.data.ref.{RefVal }
import io.circe.Encoder
import org.scalajs.dom.ext.Ajax
import scala.util.Try
import io.circe.Decoder
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag


private[caching] class ViewCache[V <: View](cacheInterface: CacheInterface ) {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  println( s"Constructor of EntityCacheMap" )

  val cacheMap = new MapForViewCache[V]

  var nrOfAjaxReqSent = 0
  var nrOfAjaxReqReturnedAndHandled = 0


  private def launchAjaxReqToGetViewResult( ref: V#Par )(
      implicit decoder: Decoder[V]],
      ct:               ClassTag[V]
    ): Unit = {

    nrOfAjaxReqSent = nrOfAjaxReqSent + 1

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val ajaxCallAsFuture: Future[RefVal[V]] = {
      val res: Future[RefVal[V]] = getEntity[V]( ref )
      res
    }

    ajaxCallAsFuture.onComplete(
      r => ajaxReqReturnHandler( r )
    )
  }



  private def ajaxReqReturnHandler(tryRes: Try[V#Res] ): Unit = {

    tryRefVal.foreach(
      rv => cacheMap.insertIntoCacheAsLoaded( rv )
    )

    if (!cacheMap.isThereStillAjaxRequestsWhichHasNotReturnedYet)
      cacheInterface.reRenderShouldBeTriggered()

    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1

  }

}
