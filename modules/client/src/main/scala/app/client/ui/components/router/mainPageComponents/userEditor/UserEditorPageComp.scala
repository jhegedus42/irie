package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cacheInjector.{
  CacheInterface,
  CacheInterfaceWrapper,
  ReactCompWrapper,
  ToBeWrappedComponent
}
import app.client.ui.components.router.mainPageComponents.{
  MainPage,
  UserEditorPage
}
import bootstrap4.TB.C
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
//import io.circe.generic._

trait UserEditorPageComp
    extends ToBeWrappedComponent[UserEditorPageComp] {

  override type Props = UserEditorPageComp.Props
  override type Backend =
    UserEditorPageComp.UserEditorBackend[UserEditorPageComp.Props]
  override type State = UserEditorPageComp.State

}

object UserEditorPageComp {

  case class State(stateString: String)
  case class Props(propsString: String)

  // this should take a uuid and edit the user

  // todo-now-3
  //  take uuid from URL and print user info
  //  such as: name + favorite number
  //


  val component: Component[
    CacheInterfaceWrapper[Props],
    State,
    UserEditorBackend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterfaceWrapper[Props]](
        "User Editor Page"
      )
      .initialState(State("initial state"))
      .renderBackend[UserEditorBackend[Props]]
      .build
  }

  class UserEditorBackend[Properties](
    $ : BackendScope[CacheInterfaceWrapper[Properties], State]) {

    def render(
      cacheInterfaceWrapper: CacheInterfaceWrapper[Props],
      s:                     State
    ): VdomElement = {
      <.div("hello")
    }

  }

  def getWrappedReactCompConstructor(
    cacheInterface:       CacheInterface,
    propsProvderFunction: () => Props
  ) = {
    val reactCompWrapper = ReactCompWrapper[UserEditorPageComp](
      cache         = cacheInterface,
      propsProvider = propsProvderFunction,
      comp          = component
    )
    reactCompWrapper.wrappedConstructor
  }

  def getRoute(cacheInterface: CacheInterface) = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val wrappedComponent = getWrappedReactCompConstructor(
        cacheInterface,
        () => Props("hello user editor page")
      )

      staticRoute("#userEditorPage", UserEditorPage) ~> render({
        wrappedComponent
      }) // todo-now => make this dynamic and pass props from the URL
  }
}
