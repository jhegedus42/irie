package app.client.ui.components.mainPages.login
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.Helpers
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{^, _}
import japgolly.scalajs.react.extra._
import org.scalajs.dom.html.Input
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^}
import japgolly.scalajs.react.{
  BackendScope,
  ReactEventFromInput,
  ScalaComponent
}
import monix.reactive.subjects.BehaviorSubject
import org.scalajs.dom.html.Input

class TextField(label: String) {
//  case class MyCallback(cb :() => () )

  var state: TextFieldState = getInitState

  def getInitState: TextFieldState = TextFieldState("initialState")

  case class TextFieldProps(label: String)

  case class TextFieldState(text: String)

  final class TextFieldBackend(
    $ : BackendScope[TextFieldProps, TextFieldState]) {


    def callback  ={ (e: ReactEventFromInput) =>
      {
        val ev:  ReactEventFromInput = e
        val t:   Input               = ev.target
        val str: String              = t.value

        println(s"new state : $str ")
        state=TextFieldState(str)
        $.setState(TextFieldState(str))
      }
    }

    def render(
      p: TextFieldProps,
      s: TextFieldState
    ): VdomElement =
      <.div(
        p.label,
        <.input.text(
          ^.onChange ==> callback,
          ^.value.:=(s.text)
        )
      )
  }

  val comp = ScalaComponent
    .builder[TextFieldProps]("TextField")
    .initialState(getInitState)
    .renderBackend[TextFieldBackend]
    //.configure(Reusability.shouldComponentUpdate)
    .build

  val builtComp = comp(TextFieldProps(label))

}
