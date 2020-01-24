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
  CoordInPixel,
  ImgFileName,
  Rect,
  SizeInPixel
}

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Bridge to [TagsInput](https://github.com/olahol/react-tagsinput)'s component
  */

@ScalaJSDefined
trait Crop extends js.Object {
  val unit:   String
  val x:      Double
  val y:      Double
  val width:  Double
  val height: Double
}

import CanProvideDefaultValue.defValOf

case class ReactCropWidgetState(
  crop:     Crop        = ReactCropWidgetState.initCrop,
  fileName: ImgFileName = defValOf[ImgFileName])

object ReactCropWidgetState {

  def rect2Crop(r: Rect): Crop = {
    new Crop {
      override val unit:   String = "px"
      override val x:      Double = r.center.x
      override val y:      Double = r.center.y
      override val width:  Double = r.size.width
      override val height: Double = r.size.height
    }
  }

  def crop2Rect(c: Crop): Rect = {
    Rect(CoordInPixel(c.x, c.y),
         SizeInPixel(c.width, c.height))
  }

  // todo - continue here

  val initCrop = new Crop {
    override val unit:   String = "px"
    override val x:      Double = 10
    override val y:      Double = 10
    override val width:  Double = 50
    override val height: Double = 50
  }
}

object ReactCrop extends ReactBridgeComponent {

  // todo-now create a simple JS Object

  def apply(
    src:  js.UndefOr[String] = js.undefined,
    crop: js.UndefOr[Crop]   = js.undefined,
    onChange: js.UndefOr[(Crop, Crop)=> Callback] =
      js.undefined
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
    .initialState(None.asInstanceOf[Option[ReactCropWidgetState]])
    .renderBackend[Backend]
    .componentWillMount(f => {

      Callback {
        externalUpdater.listen((x: Option[ReactCropWidgetState]) => {
          println(
            s"crop is updated:$x\n" +
              s"new value=${x.toString()}\n" +
              s"new Rect=${x
                .map({ c =>
                  ReactCropWidgetState.crop2Rect(c.crop)
                }).toString}\n"
          )
          f.setState(x).runNow()
        })
      }

    })
    .build

  class Backend(
    $ : BackendScope[Unit, Option[ReactCropWidgetState]]) {

    import monocle.macros.syntax.lens._

    def handlerCore(
      c:  Crop,
      cp: Crop
    ): Callback = {

      def f(oldState: ReactCropWidgetState): ReactCropWidgetState =
        oldState.lens(_.crop).set(c)

      def g(s: Option[ReactCropWidgetState]): Callback = {
        Callback {
          val ns = s.map(f)
          internalStateUpdater.send(ns)
        }
      }

      lazy val updateCell: CallbackTo[Unit] = ($.state.>>=(g))
      updateCell >> $.modState(_.map(f))

    }



    def render(
      props: Unit,
      state: Option[ReactCropWidgetState]
    ): VdomElement = {
      if (state.isDefined) {

        <.div(
          ReactCrop(
            src      = state.get.fileName.fileNameAsString,
            crop     = state.get.crop,
            onChange = handlerCore(_,_)
          )
        )

      } else {
        <.div(
          "ImgCropWidget's state is not defined. "
        )
      }

    }
  }
}
