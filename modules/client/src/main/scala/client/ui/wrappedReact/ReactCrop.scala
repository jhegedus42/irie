package client.ui.wrappedReact

import client.ui.wrappedReact.ReactCrop.componentName
import com.payalabs.scalajs.react.bridge.{ReactBridgeComponent, WithPropsNoChildren}
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

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


}

object ReactCropWrapped {


  val initCrop = new Crop {
    override val unit:   String = "px"
    override val x:      Int    = 10
    override val y:      Int    = 10
    override val width:  Int    = 50
    override val height: Int    = 50
  }

  lazy val comp = ScalaComponent
    .builder[String]("RCWrapped")
    .initialState(initCrop)
    .renderBackend[Backend]
    .build

  class Backend($ : BackendScope[String, Crop]) {

    def handlerCore(c: Crop): Callback = {
      $.setState(c)
    }

    def render(
                props:  String,
                state: Crop
              ): VdomElement = {
      <.div(
        ReactCrop(
          src = "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg",
          crop = state,
          onChange = handlerCore(_)
        )

      )
    }
  }

}