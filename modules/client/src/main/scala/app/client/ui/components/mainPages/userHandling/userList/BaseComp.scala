package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper
}
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

trait BaseComp {

//  def getWrappedComp(
//    value: RouterCtl[MainPage],
//    cache: Cache
//  ): MainPageReactCompWrapper[UserListComp, UserListPage]

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

  type Props
  type State

  def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Props],
    s: State
  ): VdomElement

//  type Backend[Props]
  class Backend[Properties](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Properties], State]) {

    def render(
      cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props],
      s:                          State
    ): VdomElement = {

      getVDOM(cacheAndPropsAndRouterCtrl, s)
    }

  }

  def getInitState: State

  val component: Component[
    CacheAndPropsAndRouterCtrl[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]](
        "Page listing all the users"
      )
      .initialState(getInitState)
      .renderBackend[Backend[Props]]
      .build
  }

  def propsProvider_ : Unit => UserListComp#PropsT

  def getWrappedComp(
                      ctl:   RouterCtl[MainPage],
                      cache: Cache
                    ): MainPageReactCompWrapper[UserListComp, UserListPage] =
    MainPageReactCompWrapper[UserListComp, UserListPage](
      cache            = cache,
      propsProvider    = propsProvider_,
      comp             = UserListComp.component,
      routerController = ctl
    )

}
