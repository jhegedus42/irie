package client.sodium.app.reactComponents

import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._
import client.sodium.core.{Cell, StreamSink}

case class STextArea(init: String) {

  private val stream: StreamSink[String] = new StreamSink[String]()
  val cell:           Cell[String]       = stream.hold(init)

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

  val component = ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(init)
    .renderBackend[Backend]
    .build

}
