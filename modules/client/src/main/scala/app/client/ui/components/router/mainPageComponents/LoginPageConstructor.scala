package app.client.ui.components.router.mainPageComponents

import app.client.ui.caching.cacheInjector.CacheInterfaceWrapper
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.SumNumberState
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object LoginPageConstructor {

  object State {
    case class IsUserLoggedIn(yesOrNo: Boolean)
    var isUserLoggedIn = State.IsUserLoggedIn(false)

    def setUserLoggedIn(): Unit = {
      isUserLoggedIn = IsUserLoggedIn(true)
    }
  }

  class LoginPageBackend[Unit](
    $ : BackendScope[Unit, State.IsUserLoggedIn]) {

    def handleLoginButton() = {
      State.setUserLoggedIn()
      $.setState(State.IsUserLoggedIn(true))

      // todo-later :
      //  maybe we should also click here programatically to redirect the app to
      //  some "logged in" page


      // todo-later
      //  we can also use here the "Re-render Callback" from
      //  RouterWrapper.ReRendering to change the Router's
      //  menu-bar, and its contents, since naturally, an application
      //  into which the user has already logged in has normally
      //  a new set of menus available in its menu-bar
      //  normally an app only gives the option to log-in, first,
      //  and only after successful login, will the app provide menu items
      //  which can be used to access the actual functionality of the app
      //  in other words, the app, without logging in, should be "useless"
      //  and the presented menus/menu-bar should be also consistent with this
      //  "principle"/"expected UI behaviour"

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
      .initialState(State.isUserLoggedIn)
      .renderBackend[LoginPageBackend[Unit]]
      .build

}
