package sodium.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._
import sodium.core.StreamSink

case class SodiumButton(name: String = "Button") {

  val streamSink = new StreamSink[Unit]()

  val getVDOM: Component[Unit, CtorType.Nullary] =
    ScalaFnComponent[Unit] { props: Unit =>
      <.div(
        <.button(name, ^.onClick --> Callback({
          println("I was pushed")
          streamSink.send(Unit)
        }))
      )
    }

}
