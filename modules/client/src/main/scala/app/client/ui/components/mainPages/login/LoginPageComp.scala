package app.client.ui.components.mainPages.login

import app.client.ui.components.MainPage
import app.client.ui.components.mainPages.login.LoginPageComp.State.{LoginPageCompState, UserLoginStatus}
import app.client.ui.dom.Window
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object LoginPageComp {
  val loginComp: TextField = new TextField("login name:")
  val passwordComp: TextField = new TextField("password:")

  case class Props(routerCtl: RouterCtl[MainPage])

  object State {
    case class LoginPageCompState(
      loginStatus: UserLoginStatus = UserLoginStatus())

    case class UserLoginStatus(
      userOption: Option[EntityWithRef[User]] = None)

    def setUserLoggedIn(user: EntityWithRef[User]): Unit = {
      Window.setLoggedInUser(user)
    }
  }

  def isUserLoggedIn: UserLoginStatus = {
    val s = Window.getUserLoginStatus
    s
  }

//    Window.getLoggedInUUID match {
//    case "in" => State.IsUserLoggedIn(true)
//    case _ =>
//      State.IsUserLoggedIn(false)
//  }

  class LoginPageBackend[P](
    $ : BackendScope[Props, State.LoginPageCompState]) {

    def handleLoginButton(p: Props): CallbackTo[Unit] = {
//      State.setUserLoggedIn()

      // todo-now - 1 make full login functionality
      //

      val refresh: Callback = p.routerCtl.refresh
//      $.setState(???) >> refresh // todo-now 1.1

      println(s"our current login name is: ${loginComp.state}")
      println(s"our current password is: ${passwordComp.state}")

      refresh
    }

    def render(
      p: Props,
      s: State.LoginPageCompState
    ): VdomElement = {

      // todo-now 1.5 RIGHT-NOW - add login-name and password fields

      val isUserLoggedIn: Boolean = s.loginStatus.userOption.isDefined

      val login =
        if (isUserLoggedIn)
          <.div(
            <.p(
              "You are now logged in ! Welcome !"
            )
          )
        else {
          import bootstrap4.TB.convertableToTagOfExtensionMethods
          <.div(
            loginComp.builtComp,
            <.br,
            passwordComp.builtComp,
            <.button.btn.btnPrimary(
              "Press this button to log in.",
              ^.onClick --> handleLoginButton(p)
            )
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

  val initState: LoginPageCompState = LoginPageCompState()

  val component =
    ScalaComponent
      .builder[Props]("Login Page")
      .initialState(initState)
      .renderBackend[LoginPageBackend[Props]]
      .build

}
