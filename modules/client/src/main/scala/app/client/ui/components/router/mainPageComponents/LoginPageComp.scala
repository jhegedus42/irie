package app.client.ui.components.router.mainPageComponents

import app.client.ui.caching.cacheInjector.CacheAndProps
import app.client.ui.components.RouterWrapper
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.SumNumberState
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object LoginPageComp {

  object State {
    case class IsUserLoggedIn(yesOrNo: Boolean)
    var isUserLoggedIn = State.IsUserLoggedIn(false)

    def setUserLoggedIn(): Unit = {
      isUserLoggedIn = IsUserLoggedIn(true)
      RouterWrapper.reRenderApp()
    }
  }
  def isUserLoggedIn =State.isUserLoggedIn

  class LoginPageBackend[Unit](
    $ : BackendScope[Unit, State.IsUserLoggedIn]) {

    def handleLoginButton() = {
      State.setUserLoggedIn()
      $.setState(State.IsUserLoggedIn(true))
    }

    def render(
      p: Unit,
      s: State.IsUserLoggedIn
    ): VdomElement = {

      <.div(
        <.main(C.justifyContentCenter ,C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            C.justifyContentCenter,
            C.justifyContentCenter,
            C.textCenter,
            <.h1("Login page"),
            <.p(C.lead, "Login Status:"),
            <.br,
            s"${s.yesOrNo}",
            <.br, {

              import bootstrap4.TB.convertableToTagOfExtensionMethods
              <.button.btn.btnPrimary(
                "Press this button to log in.",
                ^.onClick --> handleLoginButton()
              )
            }
          )
        )
      )
    }
  }

  import bootstrap4.TB.C

  val component =
    ScalaComponent
      .builder[Unit]("Login Page")
      .initialState(isUserLoggedIn)
      .renderBackend[LoginPageBackend[Unit]]
      .build

}
