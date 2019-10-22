package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.getWrappedComp
import app.client.ui.components.{
  MainPage,
  StaticTemplatePage,
  UserListPage
}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CtorType,
  ScalaComponent
}
import org.scalajs.dom

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type PropsT = UserListComp.Props
  override type BackendT =
    UserListComp.Backend[UserListComp.Props]
  override type StateT = UserListComp.State

}

object UserListComp extends BaseComp {
//  type State
//  type Props

  case class State(someString: String)

  case class Props(propString:String)

//  def getProps

  def getInitState: State = State("initial state")

  def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Props],
    s: State
  ): VdomElement = {

    UserListRenderLogic(c).getVDOM
  }

  override def propsProvider_ : Unit => Props = _ => Props("hello")
}
