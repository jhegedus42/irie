package app.client.ui.components.mainPages.userHandling.userList
//
//import app.client.ui.caching.cacheInjector.{
//  Cache,
//  CacheAndPropsAndRouterCtrl,
//  MainPageReactCompWrapper,
//  ToBeWrappedMainPageComponent
//}
//import app.client.ui.components.MainPageInjectedWithCacheAndController
//import japgolly.scalajs.react.BackendScope
//import japgolly.scalajs.react.vdom.VdomElement
////import app.client.ui.components.mainPages.userHandling.userList.UserListComp.{Backend, Props, State, getInitState}
//import app.client.ui.components.{MainPage, UserListPage}
//import japgolly.scalajs.react.{CtorType, ScalaComponent}
//import japgolly.scalajs.react.component.Scala.Component
//import japgolly.scalajs.react.extra.router.RouterCtl
//
//trait BaseComp[
//  Comp <: ToBeWrappedMainPageComponent[Comp, Page],
//  Page <: MainPageInjectedWithCacheAndController[Comp, Page]]
//    extends ToBeWrappedMainPageComponent[Comp, Page] {
//

//
//  override def getVDOM(
//    c: CacheAndPropsAndRouterCtrl[Comp#PropsT],
//    s: Comp#StateT,
//    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[
//      Comp#PropsT
//    ], Comp#StateT]
//  ): VdomElement

//  type Backend[Props]

//  def getInitState: Comp#StateT

//  override type BackendT = Backend

//  def propsProvider_ : Unit => Comp#PropsT

//}
