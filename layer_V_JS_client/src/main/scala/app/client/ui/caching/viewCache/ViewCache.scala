package app.client.ui.caching.viewCache

import app.client.ui.caching.ReRenderer
import app.client.ui.caching.viewCache.ViewCacheStates.{ViewLoading, ViewCacheState, ViewLoaded}
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.{ViewHttpRouteName, ViewHttpRouteNameProvider}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.reflect.ClassTag
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
import io.circe.generic.auto._


object SumIntViewCache {
  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  private var sumIntViewOpt: Option[ViewCacheState[SumIntView]] = None

  def getSumIntView(requestParams: SumIntView#Par): Option[ViewCacheState[SumIntView]] = {


    println
    println
    println(s" ---------------- we call `getSumIntView` and `sumIntViewOpt` is $sumIntViewOpt")
    println
    println

    if (sumIntViewOpt.isEmpty) {

      postViewRequest[SumIntView](requestParams)
        .onComplete((res: Try[SumIntView_HolderObject.SumIntView_Res]) => {

          val success: SumIntView_HolderObject.SumIntView_Res =res.get
          // this is not very nice here :(
          // one day we should introduce the "sad" path

          sumIntViewOpt=Some(ViewLoaded[SumIntView](requestParams,success))

          println(s"---------- getSumIntView --------- HAPPY PATH: ${sumIntViewOpt}")

          ReRenderer.triggerReRender()

        })
      sumIntViewOpt=Some(ViewLoading[SumIntView](requestParams))
    }

    sumIntViewOpt

  }


  private def postViewRequest[V <: View]( requestParams: V#Par ) (
                                  implicit ct: ClassTag[V],
                                  encoder: Encoder[V#Par],
                                  decoder: Decoder[V#Res]
                                ): Future[V#Res] = {

    val routeName: ViewHttpRouteName = ViewHttpRouteNameProvider.getViewHttpRouteName[V]()

    val url: String = routeName.name

    val json_line: String = requestParams.asJson.spaces2 // encode

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    val res: Future[V#Res] =
      Ajax
      .post(url, json_line, headers = headers)
      .map(_.responseText)
      .map((x: String) => { decode[V#Res](x) })
      .map(x => x.right.get)

    res
  }

}

