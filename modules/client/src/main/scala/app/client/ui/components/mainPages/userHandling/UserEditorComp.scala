package app.client.ui.components.mainPages.userHandling

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndProps, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.userHandling.UserEditorComp.UserEditorPage
import app.client.ui.components.{StaticTemplatePage, ItemPage, MainPage, MainPageWithCache}
import app.shared.comm.postRequests.{AdminPassword, GetAllUsersReq, GetEntityReq}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}

trait UserEditorComp
    extends ToBeWrappedMainPageComponent[
      UserEditorComp,
      UserEditorPage
    ] {

  override type Props = UserEditorComp.Props
  override type Backend =
    UserEditorComp.Backend[UserEditorComp.Props]
  override type State = UserEditorComp.State

}

object UserEditorComp {

  case class UserEditorPage(paramFromURL: String)
      extends MainPageWithCache[UserEditorComp, UserEditorPage]

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
        "This is a userEditor page. It demonstrates all crucial functionality."
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

      def link(
        mainPage: MainPage,
        linkText: String = "click me"
      ) =
        <.div(
          <.a(linkText,
              ^.href := cacheAndProps.props.routerCtl
                .urlFor(mainPage).value),
          cacheAndProps.props.routerCtl.setOnLinkClick(mainPage)
        )


      <.div(
        <.p("This is the UserEditor Page"),
        <.p(
          s"This is what we got from the " +
            s"URL : ${cacheAndProps.props.someString}",
          "We will use this page to edit the name and favorite number" +
            " of the user who has this UUID on the server."
        ),
        <.br
      )

    }

  }

  def getRoute(cacheInterface: Cache) = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def c(
        page: UserEditorPage,
        ctl:  RouterCtl[MainPage]
      ): MainPageReactCompWrapper[UserEditorComp, UserEditorPage] =
        MainPageReactCompWrapper[UserEditorComp, UserEditorPage](
          cache = cacheInterface,
          propsProvider =
            () => UserEditorComp.Props(page.paramFromURL, ctl),
          comp = UserEditorComp.component
        )

      def g2(t2: (UserEditorPage, RouterCtl[MainPage])) =
        c(t2._1, t2._2).wrappedConstructor

      def h2: UserEditorPage => dsl.Renderer =
        p => Renderer(rc => g2((p, rc)))

      def f = "#userEditorPageRoute" / string("[a-z-A-Z0-9]+")
          .caseClass[UserEditorPage]

      dynamicRouteCT[UserEditorPage](f).~>(h2)

  }
}


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
