package app.client.ui.components.mainPages.login

import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.Helpers
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{^, _}
import japgolly.scalajs.react.extra._
import org.scalajs.dom.html.Input

object UsernameAndPassword {

  case class Props(s: String)

  //implicit val reusabilityProps: Reusability[Props] =
  //  Reusability.derive

  case class State(
    userName: String = "name",
    password: String = "pwd")

  object State {

    def init: State =
      State()

    //implicit val reusability: Reusability[State] =
    //  Reusability.derive
  }

  final class Backend($ : BackendScope[Props, State]) {

    import monocle.macros.syntax.lens._

    def render(
      p: Props,
      s: State
    ): VdomElement =
      <.div(
        <.input.text(
          ^.onChange ==> { (e: ReactEventFromInput) =>
            {
              val ev:  ReactEventFromInput = e
              val t:   Input               = ev.target
              val str: String              = t.value

              $.modState(st => st.copy(userName = str))
            }
          },
          ^.value.:=(s.userName)
        )
      )
  }

  val Component = ScalaComponent
    .builder[Props]("UsernameAndPassword")
    .initialState(State.init)
    .renderBackend[Backend]
    //.configure(Reusability.shouldComponentUpdate)
    .build

}
