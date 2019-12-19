package client.ui.atomicWidgets.input

import client.sodium.core.{Cell, CellSink, Stream, StreamSink}
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaComponent,
  _
}

case class STextArea(
  initValue:       String,
  externalUpdater: Stream[String] = new Stream[String]()) {

  val internalUpdater = new StreamSink[String]();

  val mergedStream = externalUpdater.orElse(internalUpdater)

  val cell: Cell[String] = mergedStream.hold(initValue)

  val comp = ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(initValue)
    .renderBackend[Backend]
    .build

  class Backend($ : BackendScope[Unit, String]) {

    cell.listen(x => $.setState(x).runNow())

    def render(text: String) = {

      def onChange(e: ReactEventFromInput): Callback = {
        println("callback called, now")
        val newValue: String = e.target.value

        Callback(internalUpdater.send(newValue)) >>
          $.setState(newValue) >> Callback {
          println(s"client.state is $newValue")
        }
      }

      <.div(
        <.textarea(^.onChange ==> onChange, ^.value := cell.sample())
      )
    }
  }

}
