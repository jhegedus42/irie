package client.sodium.app.reactComponents.atomicElements

import client.sodium.core.StreamSink
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}

case class SButton(name: String = "Button") {

  val streamSink = new StreamSink[Unit]()

  val vdom: Component[Unit, CtorType.Nullary] =
    ScalaFnComponent[Unit] {
      props: Unit =>

        <.div(
                <.button(name, ^.onClick --> Callback({
                  println("I was pushed")
                  streamSink.send(Unit)
                }))
        )
    }

}
