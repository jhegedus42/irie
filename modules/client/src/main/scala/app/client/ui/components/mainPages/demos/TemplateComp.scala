package app.client.ui.components.mainPages.demos

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.demos.TemplateComp.TemplatePage
import app.client.ui.components.{ItemPage, MainPage, MainPageInjectedWithCacheAndController, StaticTemplatePage}
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.read.{AdminPassword, GetAllUsersReq}
import app.shared.comm.postRequests.{ GetEntityReq}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}

trait TemplateComp
    extends ToBeWrappedMainPageComponent[
      TemplateComp,
      TemplatePage
    ] {

  override type PropsT = TemplateComp.Props
  override type BackendT =
    TemplateComp.Backend[TemplateComp.Props]
  override type StateT = TemplateComp.State

}

/**
  *
  * This is a template page. Do not use it for anything.
  * Copy it and rename TemplateComp and TemplatePage to
  * create a new "MainPageWithCache" with all the
  * bells and whistles.
  *
  * Don't forget to put me into :
  *
  * (1) [[app.client.ui.components.router.RouterComp#routerConfig()]]
  *
  * and into
  *
  * (2) [[app.client.ui.components.router.RouterComp#mainMenu()]]
  *
  * otherwise I wont appear anywhere in the app, or some errors will
  * be thrown at runtime, if I am being left out from the first
  * location (1).
  *
  * AND
  *
  * don't forget to change the route here :
  *
  * (3) [[TemplateComp.getRoute()]]
  *
  * from "#templatePageRoute" to something that "rezonates" with the page
  * that is being created based on this template page, if you don't do that
  * then there will be conflicts of "routes", and you really don't wanna
  * see the blody fight of the ancient routes to rise again from the Gaia.
  *
  */

object TemplateComp {

  case class TemplatePage(paramFromURL: String)
      extends MainPageInjectedWithCacheAndController[TemplateComp, TemplatePage]

  case class State(someString: String)

  case class Props(
    someString: String,
    routerCtl:  RouterCtl[MainPage])

  val component: Component[
    CacheAndPropsAndRouterCtrl[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]](
        "This is a template page. It demonstrates all crucial functionality."
      )
      .initialState(State("initial state"))
      .renderBackend[Backend[Props]]
      .build
  }

  /**
    *
    * This demonstrates how to do a "flatmap" on
    * "AJAX calls".
    *
    * They can be turned into options and can be manipulated
    * as options in a for comprehension, for example.
    *
    * The page gets re-render every time once a caches
    * AJAX requests have all returned. I.e. the cache contains
    * no more "pending"/"in-flight" type of entries.
    *
    * The for comprehension in this class is a flattened
    * option: first an ajax call is sent to get all the
    * references to all users. Once that arrives, then
    * for every reference in the list an ajax call will
    * be launched, they appear as Option-s in the for
    * comprehension. Once all the AJAX requests for every
    * reference that have been sent out have returned, the
    * page gets re-rendered (triggered by the cache - which
    * initiated the AJAX calls). This is the point where the
    * List of usernames is shown to the suer.
    *
    * @param cacheInterfaceWrapper
    */
  case class RenderLogicForGettingAllUsers(
    cacheInterfaceWrapper: CacheAndPropsAndRouterCtrl[Props]) {

    val requestResultForRefToAllUsers
      : ReadCacheEntryStates.ReadCacheEntryState[ReadRequest, GetAllUsersReq] =
      cacheInterfaceWrapper.cache
        .readFromServer[ReadRequest, GetAllUsersReq](
          GetAllUsersReq.Par(AdminPassword("titok"))
        )

    val refToAllUsersOption: Option[GetAllUsersReq.Res] =
      requestResultForRefToAllUsers.toOption

    def listOfStrings2TagMod(l: List[String]): TagMod =
      TagMod(l.map(<.div(<.br, _)).toVdomArray)

    def userRef2UserOption(
      r: RefToEntityWithVersion[User]
    ): GetEntityReq.Res[User] = {
      val par_ = GetEntityReq.Par(r)
      val res_ =
        cacheInterfaceWrapper.cache
          .readFromServer[ReadRequest,  GetEntityReq[User]](par_)
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
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Properties], State]) {

    def render(
                cacheAndProps: CacheAndPropsAndRouterCtrl[Props],
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

      val renderLogic = RenderLogicForGettingAllUsers(cacheAndProps)
      val route       = StaticTemplatePage

      <.div(
        <.p("This is the Template Page"),
        <.br,
        <.hr,
        link(ItemPage(369), "link to ItemPage 369"),
        <.hr,
        <.br,
        s"result for the GetAllUsersReq is:",
        <.br,
        renderLogic.listOfUsers.getOrElse(
          TagMod("List of users is loading ...")
        ),
        <.br,
        <.hr,
        link(StaticTemplatePage, "link to AdminPage"),
        <.hr,
        <.br,
        <.p(
          s"This is what we got from the " +
            s"URL : ${cacheAndProps.props.someString}"
        ),
        <.br
      )

    }

  }

  def getRoute(cacheInterface: Cache)  = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def c(
        page: TemplatePage,
        ctl:  RouterCtl[MainPage]
      ): MainPageReactCompWrapper[TemplateComp, TemplatePage] =
        MainPageReactCompWrapper[TemplateComp, TemplatePage](
          cache = cacheInterface,
          propsProvider =
            () => TemplateComp.Props(page.paramFromURL, ctl),
          comp = TemplateComp.component,
          routerController = ctl
        )

      def page2renderer: TemplatePage => dsl.Renderer =
        p => Renderer(rc => c(p, rc).wrappedConstructor)

      def f: StaticDsl.RouteB[TemplatePage] =
        "#templatePageRoute" / string("""[\d\w-]+""").caseClass[TemplatePage]

      val res: dsl.Rule =dynamicRouteCT[TemplatePage](f).~>(page2renderer)

      res

  }
}


