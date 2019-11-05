package app.client.ui.components.mainPages.pages.userHandling.userList

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.pages.userHandling.userList
import app.client.ui.components.mainPages.pages.userHandling.userList.UserListComp.{Props$, State$}
import app.client.ui.components.sodium.SodiumWidgets
import app.client.ui.components.{MainPage,  UserListPage}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.CreateEntityReq
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}
import org.scalajs.dom
import sodium.Cell

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type PropsT = UserListComp.Props$
  override type StateT = UserListComp.State$

  def getInitState: StateT = State$(42)

  override def propsProvider_ : Unit => PropsT = _ => Props$("hello")

  object FRP {
    val sbutton = SodiumWidgets.SodiumButtom()

    val cell: Cell[String] =
      sbutton.sClickedSink.map(x => "bello").hold("hello")

    val label: SodiumWidgets.SodiumLabel =SodiumWidgets.SodiumLabel(cell)

    val vdom= <.div(
      sbutton.getVDOM(),
      <.br,
      label.comp()
    )

  }

  override def getVDOM(
    c: CacheAndPropsAndRouterCtrl[PropsT],
    s: StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[
      PropsT
    ], StateT]
  ): VdomElement = {
    import bootstrap4.TB.convertableToTagOfExtensionMethods

//    val cell
    <.div(
      "test",
      <.br,
      s"State is: ${s.counter}",
      <.br,
      <.button.btn.btnPrimary(
        "Increase Counter",
        ^.onClick --> backendScope.modState(s => s.inc)
      ),
      UserListRenderLogic(c).getVDOM,
      <.br(),
      <.br(),
      FRP.vdom
    )

  }
}

object UserListComp extends UserListComp {

  case class State$(counter: Int) {
    def inc = State$(counter + 1)
  }

  case class Props$(propString: String)

//  def getProps

}
