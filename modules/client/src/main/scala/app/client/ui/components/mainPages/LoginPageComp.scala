package app.client.ui.components.mainPages

import app.client.ui.caching.cacheInjector.CacheAndPropsAndRouterCtrl
import app.client.ui.components.MainPage
import app.client.ui.components.mainPages.LoginPageComp.State.{LoginPageCompState, UserLoginStatus}
import app.client.ui.dom.Window
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object LoginPageComp {

  case class Props(routerCtl: RouterCtl[MainPage])

  object State {
    case class TextFieldValues(
      loginName: String = "",
      password:  String = "")

    case class LoginPageCompState(
      loginStatus:     UserLoginStatus = UserLoginStatus(),
      textFieldValues: TextFieldValues = TextFieldValues())

    case class UserLoginStatus(
      userOption: Option[EntityWithRef[User]] = None)

    // todo-now 1.5.1 add password and login-name, for text-field
    def setUserLoggedIn(user: EntityWithRef[User]): Unit = {
      Window.setLoggedInUser(user)
    }
  }

  def isUserLoggedIn: UserLoginStatus = {
//    true
    ???
  }

//    Window.getLoggedInUUID match {
//    case "in" => State.IsUserLoggedIn(true)
//    case _ =>
//      State.IsUserLoggedIn(false)
//  }

  class LoginPageBackend[P](
    $ : BackendScope[Props, State.LoginPageCompState ]) {

    def handleLoginButton(p: Props): CallbackTo[Unit] = {
//      State.setUserLoggedIn()

      // todo-now - 1 make full login functionality
      //

      val refresh: Callback = p.routerCtl.refresh
      $.setState(???) >> refresh // todo-now 1.1
      refresh
    }

    def render(
      p: Props,
      s: State.LoginPageCompState
    ): VdomElement = {

      // todo-now 1.5 RIGHT-NOW - add login-name and password fields

      val isUserLoggedIn: Boolean = ??? // todo-now 1.2

      val login =
        if (isUserLoggedIn)
          <.div(
            <.p(
              "You are now logged in ! Welcome !"
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
  val initState: LoginPageCompState =LoginPageCompState()

  val component =
    ScalaComponent
      .builder[Props]("Login Page")
      .initialState(initState)
      .renderBackend[LoginPageBackend[Props]]
      .build

}
