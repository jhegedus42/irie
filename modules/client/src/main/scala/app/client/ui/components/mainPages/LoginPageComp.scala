package app.client.ui.components.mainPages

import app.client.ui.caching.cacheInjector.CacheAndProps
import app.client.ui.components.router.RouterWrapper
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
  def isUserLoggedIn = State.isUserLoggedIn

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

      val login =
        if (s.yesOrNo)
          <.div(
            <.p(
              "You are now logged in ! Yey !!! Welcome ! To the world, of tomorrow !!! :)"
            ),
            <.br,
            <.p(
              " In order to proceed to Nirvana, Zion, Valhalla, Enlightement, or call it whatever" +
                " you wanna call it, " +
                "please click  - on the top left - the " +
                "Menu called 'Home', i.e. " +
                "to see more functionality (to refresh the page). "
            ),
            <.br,
            <.p(
              "This is " +
                "needed because the router cannot be re-freshed programmatically, " +
                "or maybe it can but I have just yet not figured out how to do so. " +
                "So this text here is a 'hack', so that 'users can use the app'. " +
                "Welcome to da app :). I hope it will help you to learn a bit of " +
                "scalajs-react, or Scala, or Scala.js, or whatever floats your boat " +
                "or job, or both."
            )
          )
        else {
          import bootstrap4.TB.convertableToTagOfExtensionMethods
          <.button.btn.btnPrimary(
            "Press this button to log in.",
            ^.onClick --> handleLoginButton()
          )
        }

      <.div(
        <.main(C.justifyContentCenter,
               C.container,
               ^.role := "container")(
          <.div(C.jumbotron)(
            C.justifyContentCenter,
            C.justifyContentCenter,
            C.textCenter,
            <.h1("Login page"),
            <.p(C.lead, "Login Status:"),
            <.br,
            s"${s.yesOrNo}",
            <.br,
            login
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
