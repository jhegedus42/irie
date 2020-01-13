package client.ui.wrappedReact

import com.payalabs.scalajs.react.bridge.{
  ReactBridgeComponent,
  WithPropsNoChildren
}
import japgolly.scalajs.react.{Callback, CallbackTo}

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Bridge to [TagsInput](https://github.com/olahol/react-tagsinput)'s component
  */

@ScalaJSDefined
trait Crop extends js.Object {
  val unit:   String
  val x:      Int
  val y:      Int
  val width:  Int
  val height: Int
}

object ReactCrop extends ReactBridgeComponent {

  // todo-now create a simple JS Object

  def apply(
    src:      js.UndefOr[String]           = js.undefined,
    crop:     js.UndefOr[Crop]             = js.undefined,
    onChange: js.UndefOr[Crop => Callback] = js.undefined
  ): WithPropsNoChildren = autoNoChildren

  def handlerCore(c: Crop): Callback = {
    Callback { println(c) }
  }

}
