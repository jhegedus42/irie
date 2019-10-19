package app.client.ui.components.mainPages.login

import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.AjaxCallPar
import app.client.ui.caching.cacheInjector.ReRenderer
import app.client.ui.components.MainPage
import app.client.ui.components.mainPages.login.LoginPageComp.State.{
  LoginPageCompState,
  UserLoginStatus
}
import app.client.ui.dom.Window
import app.shared.comm.postRequests.LoginReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

object LoginPageComp {
  val loginNameComp: TextField = new TextField("login name:")
  val passwordComp:  TextField = new TextField("password:")

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

      println(s"our current login name is: ${loginNameComp.state}")
      println(s"our current password is: ${passwordComp.state}")

      // todo-now ^ check for user ... using login req
      // send

      val par = LoginReq.Par(loginNameComp.state.text,
                             passwordComp.state.text)

      val aPar: AjaxCallPar[LoginReq] = AjaxCallPar[LoginReq](par)

      implicit def executionContext: ExecutionContextExecutor =
        scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

      AJAXCalls
        .sendPostAjaxRequest[LoginReq](aPar).onComplete(
          res => {
            println(res)
            val optUser: Option[EntityWithRef[User]] =
              res.toOption.flatMap(x => x.res.optionUserRef)
            if(optUser.isDefined){

              val newState=UserLoginStatus(optUser)
              val newState2=LoginPageCompState(newState)
              val cb= $.setState(newState2) >> refresh
              State.setUserLoggedIn(optUser.get)
              cb.runNow()

            }
          }
        )

//      ReRenderer.triggerReRender()

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
            loginNameComp.builtComp,
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
