package app.client.ui.components.mainPages.userHandling

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.userHandling.UserEditorComp.UserEditorPage
import app.client.ui.components.{
  StaticTemplatePage,
  MainPage,
  UserListPage
}
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  GetEntityReq
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type Props = UserListComp.Props
  override type Backend =
    UserListComp.Backend[UserListComp.Props]
  override type State = UserListComp.State

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

  case class RenderLogic(
    cacheInterfaceWrapper: CacheAndProps[Props]) {

    val requestResultForRefToAllUsers
      : CacheEntryStates.CacheEntryState[GetAllUsersReq] =
      cacheInterfaceWrapper.cache
        .getPostReqResult[GetAllUsersReq](
          GetAllUsersReq.Par(AdminPassword("titok"))
        )

    val refToAllUsersOption: Option[GetAllUsersReq.Res] =
      requestResultForRefToAllUsers.toOption

    def optUserEntity2VDOM(
      user: Option[Entity[User]]
    ): VdomElement = {
      if (user.isDefined)
        <.div("user into :", user.toString())
      else <.div("user info not loaded yet ...")
    }

    def getUserListAsVDOM(l: List[Option[Entity[User]]]): TagMod =
      TagMod(l.map(optUserEntity2VDOM(_)).toVdomArray)

    def userRef2UserOption(
      r: RefToEntityWithoutVersion[User]
    ): GetEntityReq.Res[User] = {
      val par_ = GetEntityReq.Par(r)
      val res_ =
        cacheInterfaceWrapper.cache
          .getPostReqResult[GetEntityReq[User]](par_)
          .toOption
      val emptyResult: GetEntityReq.Res[User] =
        GetEntityReq.Res[User](None)

      res_.getOrElse(emptyResult)
    }

    def linkToUserEditorPage(
      ctl:  RouterCtl[MainPage],
      uuid: String
    ) =
      <.div(
        <.a("edit", ^.href := ctl.urlFor(StaticTemplatePage).value),
        ctl.setOnLinkClick(UserEditorPage(uuid))
      )

    val userListAsVDOM: Option[html_<^.TagMod] = for {
      res <- refToAllUsersOption
      res2 = res.allUserRefs
      res3 = res2.map(userRef2UserOption(_))
      res4 = res3.map(x => x.optionEntity)
      res5 = getUserListAsVDOM(res4)
    } yield (res5)

  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Properties], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val renderLogic = RenderLogic(cacheAndProps)
      val route       = StaticTemplatePage

      <.div(
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
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

      def r  =
        staticRoute("#userList", UserListPage())

      def page2render: dsl.Renderer =
          Renderer(rc => getWrappedComp(rc, cache).wrappedConstructor)

      def res: dsl.Rule = (r).~>(page2render)


      res
  }
}
