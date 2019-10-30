package app.client.ui.components.sodium

import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import sodium.StreamSink

case class SButton(){
  val sClickedSink = new StreamSink[Unit]

  val getVDOM: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { props: Unit =>
    <.div(
      <.button("String", ^.onClick --> Callback({
        sClickedSink.send(Unit)
      }))
    )
  }

}
