package app.client.ui.caching.viewCache

import app.client.ui.caching.REST._
import app.client.ui.caching.{REST, ReRenderer}
import app.client.ui.caching.viewCache.ViewCacheStates.{ViewCacheState, ViewLoaded, ViewLoading}
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
import io.circe.generic.auto._

object SumIntViewCache {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  //  private var cacheMap :

  private var sumIntViewOpt: Option[ViewCacheState[SumIntView]] = None
  //TODO replace this with a proper MAP

  def getSumIntView(
      requestParams: SumIntView#Par
    ): Option[ViewCacheState[SumIntView]] = {

    println
    println
    println(
      s" ---------------- we call `getSumIntView` and `sumIntViewOpt` is $sumIntViewOpt"
    )
    println
    println

    if (sumIntViewOpt.isEmpty) {

      getView[SumIntView]( requestParams )
        .onComplete( (res: Try[SumIntView_HolderObject.SumIntView_Res]) => {

          val success: SumIntView_HolderObject.SumIntView_Res = res.get
          // this is not very nice here :(
          // one day we should introduce the "sad" path

          sumIntViewOpt =
            Some( ViewLoaded[SumIntView]( requestParams, success ) )

          println(
            s"---------- getSumIntView --------- HAPPY PATH: ${sumIntViewOpt}"
          )

          ReRenderer.triggerReRender()

        } )

      sumIntViewOpt = Some( ViewLoading[SumIntView]( requestParams ) )

    }

    sumIntViewOpt

  }


}
