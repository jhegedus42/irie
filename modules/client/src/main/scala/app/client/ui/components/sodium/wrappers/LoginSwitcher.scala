package app.client.ui.components.sodium.wrappers
import scala.reflect.ClassTag
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.caching.cacheInjector.{CacheAndPropsAndRouterCtrl, ReRenderer}
import app.client.ui.components.mainPages.pages.login.LoginPageComp
import app.client.ui.components.mainPages.pages.login.LoginPageComp.{Props, State}
import app.client.ui.components.router.RouterComp
import app.client.ui.components.{LoginPage, MainPage, MainPageInjectedWithCacheAndController, UserListPage}
import com.sun.org.apache.xpath.internal.operations.Div
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html
import sodium.{Stream, StreamSink}

case class LoginSwitcher() {
  val reRender    = new StreamSink[Unit]()
  def router = RouterComp().router
  def isLoggedIn= LoginPageComp.isUserLoggedIn

  var bs:Option[BackendScope[Int,String]]=None

  reRender.listen(x=>bs.get.setState("hejj").runNow())

  class Backend[P]( $ : BackendScope[Int,String]) {

    bs=Some($)

    def render(p:Int,s:String) : VdomElement ={
      println("long in switcher was re-rendered")
      def r: VdomTagOf[html.Div] =
        if (isLoggedIn.userOption.isDefined) {
          <.div(p,"logged in", router())
        } else <.div(p,"not logged in", router())

      <.div(r,"test: "+s)
    }
  }


  val comp = ScalaComponent
    .builder[Int]("wrapped")
    .initialState("baszodjmeg")
    .renderBackend[Backend[String]]
    .build

  def setLoggedIn():  Unit = reRender.send(Unit)
}
