package client.ui.atomicWidgets.input

import client.sodium.core.StreamSink
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}

case class SButtonSendClick(name: String = "Button",
                            click:StreamSink[Unit]) {

  val comp: Component[Unit, CtorType.Nullary] =
    ScalaFnComponent[Unit] { props: Unit =>
      import bootstrap4.TB.C
      import bootstrap4.TB.convertableToTagOfExtensionMethods
      <.div(
        <.button.btn.btnPrimary(name, ^.onClick --> Callback({
          click.send(Unit)
        }))
      )
    }

}
