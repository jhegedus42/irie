package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.MainPageInjectedWithCacheAndController
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.{
  Props,
  State
}
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.vdom.VdomElement
//import app.client.ui.components.mainPages.userHandling.userList.UserListComp.{Backend, Props, State, getInitState}
import app.client.ui.components.{MainPage, UserListPage}
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl

trait BaseComp[
  Comp <: ToBeWrappedMainPageComponent[Comp, Page],
  Page <: MainPageInjectedWithCacheAndController[Comp, Page]]
    extends ToBeWrappedMainPageComponent[Comp, Page] {

//  def getWrappedComp(
//    value: RouterCtl[MainPage],
//    cache: Cache
//  ): MainPageReactCompWrapper[UserListComp, UserListPage]

//  override type BackendT = Unit

  def getStaticRoute(cache: Cache) = {

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

  def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Comp#PropsT],
    s: Comp#StateT
  ): VdomElement

//  type Backend[Props]
  class Backend[Properties](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Properties], Comp#StateT]) {

    def render(
      cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Comp#PropsT],
      s:                          Comp#StateT
    ): VdomElement = {

      getVDOM(cacheAndPropsAndRouterCtrl, s)
    }

  }

  def getInitState: Comp#StateT

  val component: Component[
    CacheAndPropsAndRouterCtrl[Comp#PropsT],
    Comp#StateT,
//    Backend[Comp#PropsT],
//    Comp#BackendT,
//    Unit,
    BackendT,
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Comp#PropsT]](
        "Page listing all the users"
      )
      .initialState(getInitState)
      .noBackend
      .render_PS({
      (cpr,s) =>
        getVDOM(cpr,s)
//          ???
      })
//      .renderBackend[Comp#BackendT]
      .build
  }

  def propsProvider_ : Unit => Comp#PropsT

  def getWrappedComp(
    ctl:     RouterCtl[MainPage],
    cache$ : Cache
  ): MainPageReactCompWrapper[Comp, Page] =
    MainPageReactCompWrapper[Comp, Page](
      cache            = cache$,
      propsProvider    = propsProvider_,
      comp             = component,
      routerController = ctl
    )

}
