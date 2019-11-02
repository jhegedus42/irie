package app.client.ui.components.mainPages.pages.userHandling.userEditor

import app.client.ui.caching.cacheInjector.CacheAndPropsAndRouterCtrl
import japgolly.scalajs.react.vdom.html_<^.{
  <,
  TagMod,
  VdomElement,
  ^,
  _
}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ReactEventFromInput,
  ScalaComponent
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Input}

object TextFieldWithButtonAndHandler {
  case class State(text: String)
  case class Props(
    stateProviderCB: String => CallbackTo[Unit],
    buttonLabel:     String,
    nameOfTheTextField:String)

  class Backend($ : BackendScope[Props, State]) {
    import bootstrap4.TB.convertableToTagOfExtensionMethods

    def render(
      p: Props,
      s: State
    ): VdomElement = {
      <.div(
        p.nameOfTheTextField,
        <.input.text(^.onChange ==> { e: ReactEventFromInput =>
          $.setState(State(e.target.value))
        }, ^.value := s.text),
        <.button.btn.btnPrimary(
          p.buttonLabel,
          ^.onClick --> p.stateProviderCB(s.text)
        )
      )

    }

  }

  def textFieldComp(initString: String) =
    ScalaComponent
      .builder[Props](
        "This is a TextField, a Button, " +
          "and a Handler, szakalla volt Kender " +
          "- which handles the button press."
      )
      .initialState(State(initString))
      .renderBackend[TextFieldWithButtonAndHandler.Backend]
      .build

}
