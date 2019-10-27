package app.client.ui.components.mainPages.login

import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.AjaxCallPar
import app.client.ui.caching.cacheInjector.ReRenderer
import app.client.ui.components.MainPage
import app.client.ui.components.mainPages.login.LoginPageComp.State.{
  LoginPageCompState,
}
import app.client.ui.dom.Window
import app.shared.comm.postRequests.LoginReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import bootstrap4.TB.C
import cats.Functor
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import monocle.macros.Lenses

import scala.reflect.ClassTag

//import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

//@Lenses

@JsonCodec
case class UserLoginStatus(
  userOption: Option[EntityWithRef[User]] = None) {}

import scala.concurrent.ExecutionContextExecutor

object LoginPageComp {
  val loginNameComp: TextField = new TextField("login name:")
  val passwordComp:  TextField = new TextField("password:")

  case class Props(routerCtl: RouterCtl[MainPage])

  object State {
    case class LoginPageCompState(
      loginStatus: UserLoginStatus = UserLoginStatus())

//    import cats.Functor
//    import cats.implicits._

    def setUserLoggedIn(user: Option[EntityWithRef[User]]): Unit = {
      val u = UserLoginStatus(user)
      Window.setLoggedInUser(u)
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

      //

      val refresh: Callback = p.routerCtl.refresh

      println(s"our current login name is: ${loginNameComp.state}")
      println(s"our current password is: ${passwordComp.state}")

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
            if (optUser.isDefined) {

              val newState  = UserLoginStatus(optUser)
              val newState2 = LoginPageCompState(newState)
              val cb        = $.setState(newState2) >> refresh
              State.setUserLoggedIn(optUser)
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

//      val isUserLoggedIn: Boolean = isUserLoggedIn.userOption.isDefined

      val login =
        if (isUserLoggedIn.userOption.isDefined)
          <.div(
            <.p(
              "You are now logged in ! Welcome !",
              <.br,
              s"You are logged in as : ${isUserLoggedIn.userOption.head.entityValue.name}"
              // todo later ^ use read request cache to get this name ^
            )
            // todo-later - add log out button
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
