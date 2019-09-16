package app.client.ui.caching.cacheInjector
import app.client.ui.caching.cache.CacheEntryStates.CacheEntryState
import app.client.ui.caching.cache.comm.PostRequestResultCache
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.shared.comm.PostRequest
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.caching.cacheInjector.{CacheAndProps, ReRenderer}
import app.client.ui.components.router.mainPageComponents.{MainPage, WrappedMainPage}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^._

private[cacheInjector] class CacheInjectorHOC[Backend, Props, State](
  toBeWrapped: Component[CacheAndProps[Props], State, Backend, CtorType.Props]) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheAndProps[Props]]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount(
        $ =>
          Callback {
            ReRenderer.setTriggerer(ReRenderTriggerer(() => {
              $.setState(Unit).runNow()
            }))
          }
      )
      .build

  class WrapperBackend($ : BackendScope[CacheAndProps[Props], Unit]) {

    def render(props: CacheAndProps[Props]) = {
      <.div(toBeWrapped(props))
    }
  }

}

case class CacheAndProps[Props](
  cache: Cache,
  props: Props)

class Cache() {

  def getPostReqResult[Req <: PostRequest](
    par: Req#ParT
  )(
    implicit
    c:       PostRequestResultCache[Req],
    decoder: Decoder[Req#ResT],
    encoder: Encoder[Req#ParT],
    ct:      ClassTag[Req],
    ct2:     ClassTag[Req#PayLoadT]
  ): CacheEntryState[Req] = c.getPostRequestResult(par)
}

trait ToBeWrappedMainPageComponent[
  Comp <: ToBeWrappedMainPageComponent[Comp, Page],
  Page    <: WrappedMainPage[Comp,Page]] {
  type Props
  type State
  type Backend
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
//  Page <: MainPage
    Page    <: WrappedMainPage[Comp,Page]]
(cache:         Cache,
  propsProvider: () => Comp#Props,
  comp:          ScalaComponent[CacheAndProps[Comp#Props], Comp#State, Comp#Backend, CtorType.Props]) {

  val wrappedConstructor =
    new CacheInjectorHOC[Comp#Backend, Comp#Props, Comp#State](comp)
      .wrapperConstructor(
        CacheAndProps[Comp#Props](
          cache = cache,
          props = propsProvider()
        )
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
  * [[app.client.ui.components.router.mainPageComponents.MainPage]]
  *
  */
private[caching] object ReRenderer {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer): Unit =
    triggerer = Some(reRenderTriggerer)

  def triggerReRender(): Unit =
    if (triggerer.nonEmpty) triggerer.head.triggerReRender()

  case class ReRenderTriggerer(triggerReRender: () => Unit)

}
