package app.client.ui.components.sodium

import app.client.ui.components.sodium.SodiumWidgets.ReRenderTriggerer
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{BackendScope, Callback, ScalaFnComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import sodium.{Cell, Stream, StreamSink}
import japgolly.scalajs.react.vdom.html_<^.{<, TagMod, VdomElement, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, CtorType, ReactEventFromInput, ScalaComponent}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Input}

case class STextArea(initialValue:String = "") {
  val stream=new StreamSink[String]()

  val cell:Cell[String] = stream.hold(initialValue);

  class Backend($ : BackendScope[Unit, String]) {
    def onChange(e: ReactEventFromInput):Callback = {
      val newValue = e.target.value
      Callback{stream.send(newValue)} >>
      $.setState(newValue)
    }

    def render(s: String) = {
      <.div(
        <.textarea(^.onChange ==> onChange,^.value:=s)
      )
    }
  }

  val component=ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(initialValue)
    .renderBackend[Backend]
    .build

}
