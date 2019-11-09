package app.client.ui.components.sodium

import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.entity.entityValue.values.Note
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaFnComponent
}
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import sodium.{Cell, CellLoop, Stream, StreamSink}
import japgolly.scalajs.react.vdom.html_<^.{
  <,
  TagMod,
  VdomElement,
  ^,
  _
}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ReactEventFromInput,
  ScalaComponent
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Input}
import sodium._

case class STextArea(init: String) {

  private val stream:StreamSink[String] = new StreamSink[String]()
  val cell: Cell[String] = stream.hold(init)

  class Backend($ : BackendScope[Unit, String]) {

    def render(text: String) = {
        def onChange(e: ReactEventFromInput): Callback = {
          println("callback called")
          val newValue: String = e.target.value

          Callback(stream.send(newValue))>>
          $.setState(newValue) >> Callback { println(s"state is $newValue")}

        }

        <.div(
          <.textarea(^.onChange ==> onChange, ^.value := cell.sample())
        )
    }

  }

  val component = ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(init)
    .renderBackend[Backend]
    .build

}
