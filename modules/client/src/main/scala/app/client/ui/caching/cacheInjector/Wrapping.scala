package app.client.ui.caching.cacheInjector
import app.client.ui.caching.cache.ReadCacheEntryStates.ReadCacheEntryState
import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerStates.WriteHandlerState
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTC
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.shared.comm.{PostRequest, PostRequestType, ReadRequest, WriteRequest}
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.caching.cacheInjector.{CacheAndPropsAndRouterCtrl, ReRenderer}
import app.client.ui.components.mainPages.pages.login.LoginPageComp
import app.client.ui.components.{LoginPage, MainPage, MainPageInjectedWithCacheAndController, UserListPage}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

class WrapperHOC[Props, State](
  toBeWrapped: Component[CacheAndPropsAndRouterCtrl[Props], State, _, CtorType.Props]) {

  def wrapperConstructor(routerCtl: RouterCtl[MainPage]) =
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount(
        $ => {
          val r1: Callback = Callback {
            ReRenderer.setTriggerer(ReRenderTriggerer(() => {
              $.setState(Unit).runNow()
            }))
          }

//          val r2: Callback = routerCtl.set(LoginPage)
//
//          val r3: Callback =
//            if (LoginPageComp.isUserLoggedIn.yesOrNo) {
//              r1
//            } else r2

          // todo-later - perhaps we should check here if the user
          //  is logged in, and if not then redirect the page
          //  to the login page ...

          r1

        }
      )
      .build

  class WrapperBackend(
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Props], Unit]) {

    def render(props: CacheAndPropsAndRouterCtrl[Props]) = {
      <.div(toBeWrapped(props))
    }
  }

}

case class CacheAndPropsAndRouterCtrl[Props](
  cache:     Cache,
  props:     Props,
  routerCtl: RouterCtl[MainPage])

class Cache() {

  def readFromServer[Req <: PostRequest[ReadRequest]](
    par: Req#ParT
  )(
    implicit
    c:       ReadCache[Req],
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[ Req] = c.getRequestResult(par)

  def writeToServer[Req <: PostRequest[WriteRequest]](
    par: Req#ParT
  )(
    implicit
    c:       WriteRequestHandlerTC[Req],
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): WriteHandlerState[Req] = c.executeRequest(par)

}

trait ToBeWrappedMainPageComponent[
  Comp <: ToBeWrappedMainPageComponent[Comp, Page],
  Page <: MainPageInjectedWithCacheAndController[Comp, Page]] {
  type PropsT
  type StateT
  type BackendT = Backend

  def getStaticRoute(routeName:String,p:Page)(cache: Cache) = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def r =
        staticRoute(s"#$routeName", p)

      def page2render: dsl.Renderer =
        Renderer(rc => getWrappedComp(rc, cache).wrappedConstructor)

      def res: dsl.Rule = (r).~>(page2render)

      res
  }

  def getInitState: Comp#StateT

  def component: Component[
    CacheAndPropsAndRouterCtrl[Comp#PropsT],
    Comp#StateT,
//    Backend,
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
      .backend(new Backend(_))
      .renderBackend
      //      .render(_.backend.render)

      //      .noBackend
      //      .render_PS({
      //      (cpr,s) =>
      //        getVDOM(cpr,s)
      ////          ???
      //      })

      //      .renderBackend[Comp#BackendT]
      .build
  }

  def propsProvider_ : Unit => Comp#PropsT

  def getVDOM(
    c: CacheAndPropsAndRouterCtrl[Comp#PropsT],
    s: Comp#StateT,
    backendScope: BackendScope[CacheAndPropsAndRouterCtrl[Comp#PropsT],Comp#StateT]

  ): VdomElement

//  def component :
//  Component[
//    CacheAndPropsAndRouterCtrl[Comp#PropsT],
//    Comp#StateT,
////    Comp#BackendT,
//    //    Comp#BackendT,
//    //    Unit,
//        BackendT,
//    CtorType.Props
//  ]

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

  class Backend(
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Comp#PropsT], Comp#StateT]) {

    def render(
      cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[
        Comp#PropsT
      ],
      s: Comp#StateT
    ): VdomElement = {

      getVDOM(cacheAndPropsAndRouterCtrl, s,$)
    }

  }

}

/**
  *
  * This is documentation.
  *
  *
  * @param cache
  * @param propsProvider
  * @param comp
  * @tparam Comp
  */
case class MainPageReactCompWrapper[
  Comp <: ToBeWrappedMainPageComponent[Comp, Page],
  Page <: MainPageInjectedWithCacheAndController[Comp, Page]
](cache:            Cache,
  propsProvider:    Unit => Comp#PropsT,
  routerController: RouterCtl[MainPage],
  comp:             ScalaComponent[CacheAndPropsAndRouterCtrl[Comp#PropsT], Comp#StateT, _, CtorType.Props]) {

  def wrappedConstructor =
//    new WrapperHOC[Comp#BackendT, Comp#PropsT, Comp#StateT](comp)
    new WrapperHOC[Comp#PropsT, Comp#StateT](comp)
      .wrapperConstructor(routerController)(
        CacheAndPropsAndRouterCtrl[Comp#PropsT](
          cache     = cache,
          props     = propsProvider(Unit),
          routerCtl = routerController
        ) // todo-later, maybe we should pass here a router ctrl to this wrapper
      )

}

/**
  *
  * Re rendering the "currently routed main page".
  *
  * "currently routed main page" = the child of the router
  * react component in the VDOM, this normally changes if
  * we change the URL, this is the whole point of the router,
  * hence the name "currently routed main page", the page to
  * which we are currently routed to - by the router - and
  * which corresponds to the route designated by the URL in
  * the browser.
  *
  * By main page I mean :
  * [[MainPage]]
  *
  */
object ReRenderer {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer): Unit =
    triggerer = Some(reRenderTriggerer)

  def triggerReRender(): Unit = {
    if (triggerer.nonEmpty) triggerer.head.triggerReRender()
    println(
      "C6A0E3FD-5F15-435B-8750-E4F8AD9F6401 trigger re-render was called"
    )
  }

  case class ReRenderTriggerer(triggerReRender: () => Unit)

}
