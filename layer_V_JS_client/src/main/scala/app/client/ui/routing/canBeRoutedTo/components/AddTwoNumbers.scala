package app.client.ui.routing.canBeRoutedTo.components

import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object ExampleHolder {
  type State = Vector[String]
  val Example = ScalaComponent
    .builder[Unit]( "Example" )
    .initialState( Vector( "hello", "world" ) )
    .renderBackend[Backend] // ← Use Backend class and backend.render
    .build

  class Backend(bs: BackendScope[Unit, State] ) {
    def render(
        s: State
      ): VdomElement = // ← Accept props, state and/or propsChildren as argument
      <.div(
        <.div( s.length, " items found:" ),
        <.ol(
          s.toTagMod( i => <.li( i ) )
        )
      )
  }
}
