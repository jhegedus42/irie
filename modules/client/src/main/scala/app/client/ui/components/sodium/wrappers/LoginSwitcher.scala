package app.client.ui.components.sodium.wrappers
import scala.reflect.ClassTag
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CtorType,
  ScalaComponent
}
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.caching.cacheInjector.{
  CacheAndPropsAndRouterCtrl,
  ReRenderer
}
import app.client.ui.components.mainPages.pages.login.LoginPageComp
import app.client.ui.components.router.RouterComp
import app.client.ui.components.{
  LoginPage,
  MainPage,
  MainPageInjectedWithCacheAndController,
  UserListPage
}
import com.sun.org.apache.xpath.internal.operations.Div
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import sodium.{Stream, StreamSink}

case class LoginSwitcher() {
  val reRender    = new StreamSink[Boolean]()
  def notLoggedIn = RouterComp().routerCompNotLoggedIn
  def loggedIn    = RouterComp().routerCompLoggedIn

  val comp = ScalaComponent
    .builder[Int]("wrapped")
    .initialState(false)
    .render_PS(
      (p: Int, s: Boolean) => {

        val r =
          if (s)
            notLoggedIn()
          else loggedIn()

        r
      }
    ).componentDidMount($ => {
      Callback {
        reRender.listen(x => $.setState(x))
      }
    }).build
  def setLoggedIn():  Unit = reRender.send(true)
  def setLoggedOut(): Unit = reRender.send(false)
}
