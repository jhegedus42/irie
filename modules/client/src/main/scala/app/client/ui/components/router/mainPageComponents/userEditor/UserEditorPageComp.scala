package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cacheInjector.CacheInterfaceWrapper
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersBackend
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  ReactEventFromInput
}
import japgolly.scalajs.react.vdom.html_<^.{
  <,
  TagMod,
  VdomElement,
  ^,
  _
}

import bootstrap4.TB.C

object UserEditorPageComp {
  case class State(stateString: String)
  case class Props(propsString: String)

  // this should take a uuid and edit the user
  // todo-now-3
  //  take uuid from URL and print user info
  //  such as: name + favorite number
  //
  // todo-now-4
  //  make a component that prints a string passed
  //  to it to the screen
  //   inspiration :  copy it from ItemPage

  val component = {
    ScalaComponent
      .builder[CacheInterfaceWrapper[Props]](
        "User Editor Page"
      )
      .initialState(State("initial state"))
      .renderBackend[Backend[Props]] // ← Use Backend class and backend.render
      .build
  }

  class Backend[Properties](
    $ : BackendScope[CacheInterfaceWrapper[Properties], State]) {

    def render(
      props: CacheInterfaceWrapper[Properties],
      s:     State
    ): VdomElement = {
      <.div(
        <.main(C.container, ^.role := "container")
        (
          <.div(C.jumbotron)
          (
            <.h1("User editor page."),
            <.p(C.lead, "User CRUD comes here")

            // todo-now-0 - CRUD user GUI
            // todo-now-1 - list user names
            // todo-now-2 - list user refs
            // todo-now-8 : display all user Refs, here
          )
        )
      )
    }

  }

}
