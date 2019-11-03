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
import sodium.StreamSink

class ReDrawWrapper(component : () => VdomElement ) {
  private var reRender = new StreamSink[Unit]()

  val comp=ScalaComponent
    .builder[Int]("wrapped")
    .initialState(0)
    .render_PS(
      (p: Int, s: Int) => {
        val x = <.div(p, s, <.br, component() ,"string")
        x
      }
    ).componentDidMount($ => {
      Callback {
        reRender.listen(x => $.modState(s => s + 1).runNow())
      }
    }).build

  def reFresh() : Unit =reRender.send(Unit)
}
