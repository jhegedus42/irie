package app.client.ui.components.mainPages

import app.client.ui.caching.cacheInjector.CacheAndPropsAndRouterCtrl
import app.client.ui.components.MainPage
import app.client.ui.dom.Window
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object LoginPageComp {

  // todo-now - make a simple, log in, log out functionality

  // todo-now - when refreshing the app (any page)
  //  and the user is logged in, then it should be "kept"
  //  logged in, currently the login status is stored here
  //  it should be stored somewhere elsewhere ...
  //  perhaps in cookies, or whereever such information is
  //  usually stored in web apps, we need to,
  //  look into this issue later

  case class Props(routerCtl: RouterCtl[MainPage])

  object State {
    case class IsUserLoggedIn(yesOrNo: Boolean)
//    var isUserLoggedIn = State.IsUserLoggedIn(false)

    def setUserLoggedIn(): Unit = {

//      isUserLoggedIn = IsUserLoggedIn(true)
//      RouterWrapper.reRenderApp()

      Window.setLoggedInUUID("in")
    }
  }

  def isUserLoggedIn = Window.getLoggedInUUID match {
    case "in" => State.IsUserLoggedIn(true)
    case _ =>
      State.IsUserLoggedIn(false)
  }

  class LoginPageBackend[P](
    $ : BackendScope[Props, State.IsUserLoggedIn]) {

    def handleLoginButton(p: Props): CallbackTo[Unit] = {
      State.setUserLoggedIn()
      val refresh: Callback = p.routerCtl.refresh
      $.setState(State.IsUserLoggedIn(true)) >> refresh
    }

    def render(
      p: Props,
      s: State.IsUserLoggedIn
    ): VdomElement = {

      val login =
        if (s.yesOrNo)
          <.div(
            <.p(
              "You are now logged in ! Yey !!! Welcome ! To the world, of tomorrow !!! :)"
            )
          )
        else {
          import bootstrap4.TB.convertableToTagOfExtensionMethods
          <.button.btn.btnPrimary(
            "Press this button to log in.",
            ^.onClick --> handleLoginButton(p)
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
      .builder[Props]("Login Page")
      .initialState(isUserLoggedIn)
      .renderBackend[LoginPageBackend[Props]]
      .build

}
