package client.sodium.app.reactComponents.atomicElements

import client.sodium.core.{Cell, StreamSink}
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaComponent,
  _
}

case class STextArea(init: String) {

  private val stream: StreamSink[String] = new StreamSink[String]()

  val cell: Cell[String] = stream.hold(init)

  val vdom = ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(init)
    .renderBackend[Backend]
    .build

  class Backend($ : BackendScope[Unit, String]) {

    def render(text: String) = {
      def onChange(e: ReactEventFromInput): Callback = {
        println("callback called")
        val newValue: String = e.target.value

        Callback(stream.send(newValue)) >>
          $.setState(newValue) >> Callback {
          println(s"client.state is $newValue")
        }
      }
      <.div(
              <.textarea(^.onChange ==> onChange,
                         ^.value := cell.sample())
      )
    }
  }

}
