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
import app.client.ui.components.mainPages.login.LoginPageComp
import app.client.ui.components.{LoginPage, MainPage, MainPageInjectedWithCacheAndController}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

class WrapperHOC[Backend, Props, State](
  toBeWrapped: Component[CacheAndPropsAndRouterCtrl[Props], State, Backend, CtorType.Props]) {

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

  def readFromServer[RT <: ReadRequest, Req <: PostRequest[RT]](
    par: Req#ParT
  )(
    implicit
    c:       ReadCache[RT, Req],
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): ReadCacheEntryState[RT, Req] = c.getRequestResult(par)

  def writeToServer[RT <: WriteRequest, Req <: PostRequest[RT]](
    par: Req#ParT
  )(
    implicit
    c:       WriteRequestHandlerTC[RT, Req],
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
  type BackendT

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
  propsProvider:    () => Comp#PropsT,
  routerController: RouterCtl[MainPage],
  comp:             ScalaComponent[CacheAndPropsAndRouterCtrl[Comp#PropsT], Comp#StateT, Comp#BackendT, CtorType.Props]) {

  def wrappedConstructor =
    new WrapperHOC[Comp#BackendT, Comp#PropsT, Comp#StateT](comp)
      .wrapperConstructor(routerController)(
        CacheAndPropsAndRouterCtrl[Comp#PropsT](
          cache     = cache,
          props     = propsProvider(),
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
private[caching] object ReRenderer {
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
