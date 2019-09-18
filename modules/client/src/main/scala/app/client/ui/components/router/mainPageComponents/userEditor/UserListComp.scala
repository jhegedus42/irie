package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent,
  WrapperHOC
}
import app.client.ui.components.router.mainPageComponents.{
  AdminPage,
  MainPage,
  MainPageWithCache,
  SumIntPage,
  UserListPage
}
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  GetEntityReq,
  SumIntRoute
}
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import bootstrap4.TB.C
import japgolly.scalajs.react.extra.router.{
  Renderer,
  RouterConfigDsl,
  RouterCtl,
  StaticDsl
}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import io.circe.generic._
import java.io

import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.StateAndProps.SumNumbersProps
import app.client.ui.components.router.{Pages, RouterComp}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}

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

// todo-later
//  perhaps one could later give a bit more structure to pages like this
//  and formalize/enforce that structure in the ToBeWrappedComponent
//  trait, for example

object UserListComp {



  case class State(someString: String)

  case class Props(
    someString: String,
    routerCtl:  RouterCtl[MainPage])

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

    def listOfStrings2TagMod(l: List[String]): TagMod =
      TagMod(l.map(<.div(<.br, _)).toVdomArray)

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

    val listOfUsers: Option[html_<^.TagMod] = for {
      res <- refToAllUsersOption
      res2 = res.allUserRefs
      res3 = res2.map(userRef2UserOption(_))
      res4 = res3.map(
        x =>
          x.optionEntity
            .map(_.entityValue.name)
            .getOrElse("name not loaded yet ...")
      )
      res5 = listOfStrings2TagMod(res4)
    } yield (res5)

  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Properties], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val renderLogic = RenderLogic(cacheAndProps)
      val route       = AdminPage

      <.div(
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
        <.br,
        renderLogic.listOfUsers.getOrElse(
          TagMod("List of users is loading ...")
        ),
        <.br,
        <.p(
          s"This is what we got from the " +
            s"URL : ${cacheAndProps.props.someString}"
        ),
        <.div(
        <.a("click this",
            ^.href := cacheAndProps.props.routerCtl
              .urlFor(AdminPage).value),
          cacheAndProps.props.routerCtl.setOnLinkClick(AdminPage)
        )
      )

      // todo-now
      //
      // - create a page which takes an UUID from the URL
      //   and edits the corresponding User's name and
      //   favorite number
      //
      // - it will have a save button to call the Update
      //   request, which will also trigger a cache invalidation
      //   and also a page refresh,
      //
      // - for now, we use a simple, hand written form of
      //   cache invalidation :
      //
      //   a single, simple, plain update request will invalidate
      //   the entry in the local client cache for the entity
      //   which has been updated on the server (due to the
      //   execution of the update request by the server)
      //
      //   the update AJAX request will also trigger a
      //   re-render when it comes back and the cache
      //   will need to-re-fetch the invalidated/stale
      //   entity by launching an AJAX call
      //
      //   the entity cache should have a "State" that
      //   the entity is being in the process of being
      //   updated ...
      //
      //   so the update request should update the cache
      //   and make it "valid again", by updating the result
      //   so, an updateEntity request should be also a
      //   getEntity request, simulataniously, if it returns
      //   it should return with the updated entity, updated
      //   version number, etc, and insert that into the
      //   cache which had a stale entry until now, but with the
      //   return of the update AJAX request, the stale entry
      //   will become "fresh" again, by inserting the fresh value
      //   brough back by the returning update request into
      //   the place of the stale entry
      //
      //   or ...
      //
      //   when the update returns, it simply triggers a re-render
      //   and at that point the cache launches a get entity request
      //   to make its stale entity fresh again
      //
      //   so it can go like this - as well (I think I prefer this way):
      //
      //   up-to-date => update-request-sent => update-request-returned =>
      //   getEntity-AKA-refresh-request-sent => getEntity-returned-entry-is-
      //   refreshed (not-stale-any-longer)
      //
      //

    }

  }

  def getRoute(cacheInterface: Cache) = {

    import japgolly.scalajs.react.extra.router._
//    def dynRenderRJ[A <% VdomElement](
//      g: (UserListPage, RouterCtl[MainPage]) => A
//    ): UserListPage => Renderer[MainPage] =
//      p => Renderer(r => g(p, r))

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

//      dynamicRouteCT[UserListPage](
//        "#userList" / string("[a-zA-Z]+").caseClass[UserListPage]
//      ) ~> dynRender {
//          page: UserListPage =>
//        MainPageReactCompWrapper[UserListComp, UserListPage](
//          cache = cacheInterface,
//          propsProvider =
//            () => UserListComp.Props(page.paramFromURL),
//          comp = UserListComp.component
//        ).wrappedConstructor
//
//    }

//      def g = { page: UserListPage =>
//        MainPageReactCompWrapper[UserListComp, UserListPage] (
//          cache         = cacheInterface,
//          propsProvider = () => UserListComp.Props(page.paramFromURL),
//          comp          = UserListComp.component
//        ).wrappedConstructor
//      }
//
//      def h=dynRender { g }
//
//      def f= "#userList" / string("[a-zA-Z]+").caseClass[UserListPage]
//
//      dynamicRouteCT[UserListPage](f).~>(h)
//
      def c(
        page: UserListPage,
        ctl:  RouterCtl[MainPage]
      ): MainPageReactCompWrapper[UserListComp, UserListPage] =
        MainPageReactCompWrapper[UserListComp, UserListPage](
          cache = cacheInterface,
          propsProvider =
            () => UserListComp.Props(page.paramFromURL, ctl),
          comp = UserListComp.component
        )

//      def g(page: UserListPage) = c(page).wrappedConstructor

      def g2(t2: (UserListPage, RouterCtl[MainPage])) =
        c(t2._1, t2._2).wrappedConstructor

//      def h: UserListPage => dsl.Renderer = dynRender(g)
      def h2: UserListPage => dsl.Renderer =
        p => Renderer(rc => g2((p, rc)))

//      def h2= dynRenderRJ(g2)

      def f =
        "#userList" / string("[a-zA-Z]+").caseClass[UserListPage]

      dynamicRouteCT[UserListPage](f).~>(h2)

//      def pf: MainPage => Option[UserListPage] = ???

//      dynamicRouteF[UserListPage](f)(pf).~>(h)

  }
}
