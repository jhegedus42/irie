package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.userHandling.userList
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.getWrappedComp
import app.client.ui.components.{MainPage, StaticTemplatePage, UserListPage}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}
import org.scalajs.dom

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type PropsT = UserListComp.Props
//  override type BackendT =
//    UserListComp.Backend[UserListComp.Props]
  override type StateT = UserListComp.State

}

object UserListComp
    extends BaseComp[UserListComp, UserListPage]
    with UserListComp {

  type State = State$
  type Props = Props$

  case class State$(counter: Int){
    def inc: userList.UserListComp.State = State$(counter+1)
  }

  case class Props$(propString: String)

//  def getProps

  def getInitState: State = State$(42)

  override def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Props],
    s: State,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[
      Props
    ], State]
  ): VdomElement = {
    import bootstrap4.TB.convertableToTagOfExtensionMethods

    <.div("test",
      <.br,
      s"State is: ${s.counter}",
      <.br,
       <.button.btn.btnPrimary(
         "Increase Counter",
         ^.onClick --> backendScope.modState(s=>s.inc)
       ),
    UserListRenderLogic(c).getVDOM
    )

  }

  override def propsProvider_ : Unit => Props = _ => Props$("hello")
}
