package app.client.ui.caching.viewCache

import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded, ViewLoading}
import app.client.ui.caching.ReRenderer
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
import io.circe.generic.auto._

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object SumIntViewCache {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private var sumIntViewOpt: Option[ViewCacheState[SumIntView]] = None


  def getSumIntView( requestParams: SumIntView#Par ):
    Option[ViewCacheState[SumIntView]] = {

      if (sumIntViewOpt.isEmpty) {
//        REST_ForView.getView[SumIntView]( requestParams )
//          .onComplete( handleAjaxResult(requestParams) )

        sumIntViewOpt = Some( ViewLoading[SumIntView]( requestParams ) )
      }
      sumIntViewOpt
    }


  private def handleAjaxResult(requestParams: SumIntView_HolderObject.SumIntView_Par) = {
    (res: Try[SumIntView_HolderObject.SumIntView_Res]) => {

      sumIntViewOpt =
        Some(ViewLoaded[SumIntView](requestParams, res.get))

      ReRenderer.triggerReRender()

    }
  }
}
