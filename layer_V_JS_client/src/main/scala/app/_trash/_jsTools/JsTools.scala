package app._trash._jsTools

import app.shared.data.utils.model.ImgUrl
import org.scalajs.dom
import org.scalajs.dom.raw.{Element, Event, HTMLDocument, HTMLImageElement}

import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


case class ImgDim(url:ImgUrl, w:Int, h:Int)

@JSExport
object ImgTools {
  def getDimension(url:ImgUrl) : ( Future[ImgDim] ) =
  {
    val i: HTMLImageElement = dom.document.createElement("img").asInstanceOf[HTMLImageElement]

    def nh: Int = i.naturalHeight
    def nw: Int = i.naturalWidth

    val p: Promise[ImgDim] =Promise()

    def printOnload(e: Event) = {
      println(s"stuff loaded, event was:$e height: $nh width: $nw")
      p.success(ImgDim(url=url,h=nh,w=nw))
    }

    i.onload = printOnload _ // some js will call this at some point
    i.src = url.url
    p.future
  }
  @JSExport
  def printDimensionAwait(url:String) = {
    getDimension(ImgUrl(url)).onComplete(f=>println(s" $url getDimensions completed:$f"))
  }
}


@js.native
object vkbeautify extends js.Object {
  def xml(text: String): String = js.native
}
