package app.client.ui.components.mainPages.userHandling.userEditor
import app.client.ui.caching.cacheInjector.CacheAndProps
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
import org.scalajs.dom.html.Input

object TextField {
  case class State(text: String)

  def onChangeText(
    bs: BackendScope[Unit, State]
  )(e:  ReactEventFromInput
  ): CallbackTo[Unit] = {
    val event:  _root_.japgolly.scalajs.react.ReactEventFromInput = e
    val target: Input                                             = event.target
    val text:   String                                            = target.value
    bs.setState(State(text))
  }

  class Backend($ : BackendScope[Unit, State]) {

    def render(s: State): VdomElement =
      <.input.text(^.onChange ==> onChangeText($), ^.value := s.text)

  }

  def textFieldComp(initString: String) =
    ScalaComponent
      .builder[Unit](
        "This is a userEditor page. It demonstrates all crucial functionality."
      )
      .initialState(State(initString))
      .renderBackend[TextField.Backend]
      .build

}
