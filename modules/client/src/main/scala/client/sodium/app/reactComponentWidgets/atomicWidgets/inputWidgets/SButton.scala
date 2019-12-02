package client.sodium.app.reactComponentWidgets.atomicWidgets.inputWidgets

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  Callback,
  CtorType,
  ScalaFnComponent
}

case class SButton(
  name:     String = "Button",
  callBack: () => Unit) {

//  private val streamSink = new StreamSink[Unit]()
//  def getClick: Stream[Unit] = streamSink

  val vdom: Component[Unit, CtorType.Nullary] =
    ScalaFnComponent[Unit] { props: Unit =>
      <.div(
        <.button(name, ^.onClick --> Callback({
          println("I was pushed")
//          streamSink.send(Unit)
          callBack()
        }))
      )
    }

}
