package client.ui.atomicWidgets.input

import client.sodium.core.StreamSink
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

  private val streamSink = new StreamSink[Unit]()
  def getClick           = streamSink

  val vdom: Component[Unit, CtorType.Nullary] =
    ScalaFnComponent[Unit] { props: Unit =>
      import bootstrap4.TB.C
      import bootstrap4.TB.convertableToTagOfExtensionMethods
      <.div(
        <.button.btn.btnPrimary(name,
                                ^.onClick --> Callback({
                                  streamSink.send(Unit)
                                  callBack()
                                }))
      )
    }

}
