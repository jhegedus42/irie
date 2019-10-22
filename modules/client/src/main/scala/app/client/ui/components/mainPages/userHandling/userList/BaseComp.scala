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

  override def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Comp#PropsT],
    s: Comp#StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[
      Comp#PropsT
    ], Comp#StateT]
  ): VdomElement

//  type Backend[Props]

  def getInitState: Comp#StateT

  override type BackendT = Backend

  def propsProvider_ : Unit => Comp#PropsT

}
