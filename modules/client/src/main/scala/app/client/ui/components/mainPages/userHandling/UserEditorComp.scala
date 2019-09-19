package app.client.ui.components.mainPages.userHandling

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndProps, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.userHandling.UserEditorComp.UserEditorPage
import app.client.ui.components.{AdminPage, ItemPage, MainPage, MainPageWithCache}
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
