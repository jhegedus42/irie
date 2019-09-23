package app.client.ui.components.mainPages.userHandling.userEditor
import app.client.ui.caching.cacheInjector.CacheAndProps
import japgolly.scalajs.react.vdom.html_<^.{<, TagMod, VdomElement, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, CtorType, ReactEventFromInput, ScalaComponent}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Input}

object TextField {
  case class State(text: String)
  case class Props(updateHandler:String => CallbackTo[Unit])

  def onChangeText(
    bs: BackendScope[Props, State]
  )(e:  ReactEventFromInput
  ): CallbackTo[Unit] = {
    val event:  _root_.japgolly.scalajs.react.ReactEventFromInput = e
    val target: Input                                             = event.target
    val text:   String                                            = target.value
    bs.setState(State(text))
  }

  class Backend($ : BackendScope[Props, State]) {

    def saveButton(handler: CallbackTo[Unit]): VdomTagOf[Button] = {
      import bootstrap4.TB.convertableToTagOfExtensionMethods

      <.button.btn.btnPrimary(
        "Save changes.",
        ^.onClick --> handler
      )
    }

    def render(p:Props,s: State): VdomElement = {
      <.div(
        <.input.text(^.onChange ==> onChangeText($), ^.value := s.text),
        saveButton(p.updateHandler(s.text))
      )

    }

  }

  def textFieldComp(initString: String) =
    ScalaComponent
      .builder[Props](
        "This is a userEditor page. It demonstrates all crucial functionality."
      )
      .initialState(State(initString))
      .renderBackend[TextField.Backend]
      .build

}
