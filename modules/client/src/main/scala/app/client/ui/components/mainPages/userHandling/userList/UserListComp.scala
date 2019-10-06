package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.UserEditorPage
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.Props
import app.client.ui.components.{
  MainPage,
  StaticTemplatePage,
  UserListPage
}
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  GetEntityReq
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import org.scalajs.dom.html.Div

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

object UserListComp {

  case class State(someString: String)

  case class Props(routerCtl: RouterCtl[MainPage])

  val component: Component[
    CacheAndProps[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndProps[Props]](
        "Page listing all the users"
      )
      .initialState(State("initial state"))
      .renderBackend[Backend[Props]]
      .build
  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Properties], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val renderLogic = UserListRenderLogic(cacheAndProps)
      val route       = StaticTemplatePage

      <.div(
        s"Users:",
        <.br,
        renderLogic.userListAsVDOM.getOrElse(
          TagMod("List of users is loading ...")
        ),
        <.br
      )

    }

  }

  private def getWrappedComp(
    ctl:   RouterCtl[MainPage],
    cache: Cache
  ): MainPageReactCompWrapper[UserListComp, UserListPage] =
    MainPageReactCompWrapper[UserListComp, UserListPage](
      cache         = cache,
      propsProvider = () => UserListComp.Props(ctl),
      comp          = UserListComp.component
    )

  def getRoute(cache: Cache) = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def r =
        staticRoute("#userList", UserListPage())

      def page2render: dsl.Renderer =
        Renderer(rc => getWrappedComp(rc, cache).wrappedConstructor)

      def res: dsl.Rule = (r).~>(page2render)

      res
  }

}
