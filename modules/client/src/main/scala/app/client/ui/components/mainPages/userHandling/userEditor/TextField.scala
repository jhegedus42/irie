package app.client.ui.components.mainPages.userHandling.userEditor
import japgolly.scalajs.react.vdom.Attr.Event
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ScalaComponent
}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}

object TextField {
  case class Props(text: String)
  case class State(text: String)

  class Backend($ : BackendScope[Props, State]) {

    def render(
      p: Props,
      s: State
    ): VdomElement = <.div(<.p(s"State: ${s.text}"),
          <.p(s"Props: ${p.text}"),
          <.p(s"State: ${s.text}"),
      <.input.text(^.value := "bla", ^.onChange ==> {
            e => Callback(println(s"this is the event : $e"))
          }))
  }

  val textFieldComp = ScalaComponent
    .builder[TextField.Props](
      "This is a userEditor page. It demonstrates all crucial functionality."
    )
    .initialState(State("initial state"))
    .renderBackend[TextField.Backend]
    .build

}
