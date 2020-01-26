package client.ui.atomicWidgets.show

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import bootstrap4.TB.C
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}
import bootstrap4.TB.convertableToTagOfExtensionMethods
import japgolly.scalajs.react.component.Scala.Component
import org.scalajs.dom.html.Div
import scalacss.internal.Literal.Typed.hidden

case class HiderWidget(
  name:          String,
  val component: Component[Unit, Unit, Unit, CtorType.Nullary]) {

  lazy val compInstance: VdomTagOf[Div] = <.div(component())

  case class State(hidden: Boolean)

  class Backend($ : BackendScope[Unit, State]) {

    def hiddenComp(s: State): VdomTagOf[Div] = {
      if (s.hidden) {
        <.div("Component hidden.")
      } else <.div(compInstance)
    }

    def render(s: State) =
      <.div(
        <.hr,
        <.h2(name),
        <.br,
        s"Is hidden ? ${s.hidden}",
        <.br,
        <.button.btn.btnPrimary("hide/show", ^.onClick --> {
          $.modState({ (s: State) =>
            State(!s.hidden)
          })
        }),
        <.br,
        hiddenComp(s),
        <.br
      )
  }

  val hider = ScalaComponent
    .builder[Unit]("Hider")
    .initialState(State(true))
    .renderBackend[Backend]
    .build

}
