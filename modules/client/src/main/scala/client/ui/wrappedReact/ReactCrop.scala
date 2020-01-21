package client.ui.wrappedReact

import client.sodium.core.{Cell, Stream, StreamSink}
import client.ui.wrappedReact.ReactCrop.componentName
import com.payalabs.scalajs.react.bridge.{
  ReactBridgeComponent,
  WithPropsNoChildren
}
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  ScalaComponent
}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import shared.dataStorage.model.{
  CanProvideDefaultValue,
  ImgFileName,
  Rect
}

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

import CanProvideDefaultValue.defValOf

case class ReactCropWidgetState(
  crop:     Crop        = ReactCropWidgetState.initCrop,
  fileName: ImgFileName = defValOf[ImgFileName])

object ReactCropWidgetState {

  def rect2Crop(r: Rect): Crop = ???

  def crop2Rect(r: Crop): Rect = ???

  // todo - continue here

  val initCrop = new Crop {
    override val unit:   String = "px"
    override val x:      Int    = 10
    override val y:      Int    = 10
    override val width:  Int    = 50
    override val height: Int    = 50
  }
}

object ReactCrop extends ReactBridgeComponent {

  // todo-now create a simple JS Object

  def apply(
    src:      js.UndefOr[String]           = js.undefined,
    crop:     js.UndefOr[Crop]             = js.undefined,
    onChange: js.UndefOr[Crop => Callback] = js.undefined
  ): WithPropsNoChildren = autoNoChildren

}

case class ImgCropWidget(
  externalUpdater: Stream[Option[ReactCropWidgetState]]) {

  lazy val internalStateUpdater =
    new StreamSink[Option[ReactCropWidgetState]]()

  lazy val stateCell = internalStateUpdater
    .orElse(externalUpdater).hold(None)

  lazy val comp = ScalaComponent
    .builder[Unit]("ReactCropWidget")
    .initialState(ReactCropWidgetState())
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        stateCell.listen((x: Option[ReactCropWidgetState]) => {
          f.setState(x).runNow()
        })
      }

    })
    .build

  class Backend($ : BackendScope[Unit, Option[ReactCropWidgetState]]) {

    def handlerCore(
      c: Crop,
      s: Option[ReactCropWidgetState]
    ): Callback = {
      Callback {
        internalStateUpdater.send(s.copy(crop = c))
      }

    }

    def render(
      props: Unit,
      state: Option[ReactCropWidgetState]
    ): VdomElement = {
      if(state.isDefined){

      <.div(

        ReactCrop(
          src      = "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg",
          crop     = state.get.crop,
          onChange = handlerCore(_, state)
        )
      )}
        else{
          <.div(
            "ImgCropWidget's state is not defined. "
          )
        }

    }
  }
}
