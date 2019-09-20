package app.client.ui.components.mainPages.userHandling.userEditor

import app.client.ui.caching.cache.CacheConvenienceFunctions
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.{
  Props,
  UserEditorPage
}
import app.client.ui.components.{MainPage, MainPageWithCache}
import app.shared.comm.postRequests.GetEntityReq
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
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
    userIdentity: EntityIdentity,
    routerCtl:    RouterCtl[MainPage])

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
      val ent: Option[Entity[User]] =
        CacheConvenienceFunctions.getEntity[User](
          cacheAndProps.props.userIdentity,
          cacheAndProps.cache
        )

      import org.scalajs.dom.html.{Anchor, Div}

      def g[V](v: Option[V])(f: V => VdomTagOf[Div]): VdomTagOf[Div] =
        if (ent.isEmpty) <.div(<.p("loading ..."))
        else f(v.get)

      <.div(
        <.h1("This is the UserEditor Page"),
        <.br,
        <.p(
          s"User's uuid : ${cacheAndProps.props.userIdentity}"
        ),
        <.br,
        g(ent) { e =>
          <.div(
            <.br,
            <.p(s"User's name: ${e.entityValue.name}"),

            TextField.textFieldComp(
              TextField.Props(
                s"${e.entityValue.name}"
              )
            )

          )
        }
      )

    }

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
