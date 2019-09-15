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
import app.client.ui.caching.cacheInjector.{
  CacheInterfaceWrapper,
  ReRenderer
}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^._

private[cacheInjector] class CacheInjectorHOC[Backend, Props, State](
                                                                      toBeWrapped: Component[CacheInterfaceWrapper[Props], State, Backend, CtorType.Props]) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheInterfaceWrapper[Props]]("Wrapper")
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

  class WrapperBackend(
                        $ : BackendScope[CacheInterfaceWrapper[Props], Unit]) {

    def render(props: CacheInterfaceWrapper[Props]) = {
      <.div(toBeWrapped(props))
    }
  }

}

case class CacheInterfaceWrapper[Props](
  cacheInterface: CacheInterface,
  props:          Props)

class CacheInterface() {

  def getPostReqResult[Req <: PostRequest](
    par: Req#ParT
  )(
    implicit c: PostRequestResultCache[Req],
    decoder:    Decoder[Req#ResT],
    encoder:    Encoder[Req#ParT],
    ct:         ClassTag[Req],
    ct2:        ClassTag[Req#PayLoadT]
  ): CacheEntryState[Req] = c.getPostRequestResultCacheState(par)
}

trait ToBeWrappedComponent[Comp] {
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
case class ReactCompWrapper[Comp <: ToBeWrappedComponent[Comp]](
  cache:         CacheInterface,
  propsProvider: () => Comp#Props,
  comp:          ScalaComponent[CacheInterfaceWrapper[Comp#Props], Comp#State, Comp#Backend, CtorType.Props]) {

  val wrappedConstructor =
    new CacheInjectorHOC[Comp#Backend, Comp#Props, Comp#State](comp)
      .wrapperConstructor(
        CacheInterfaceWrapper[Comp#Props](
          cacheInterface = cache,
          props          = propsProvider()
        )
      )

}

private[caching] object ReRenderer {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer): Unit =
    triggerer = Some(reRenderTriggerer)

  def triggerReRender(): Unit =
    if (triggerer.nonEmpty) triggerer.head.triggerReRender()

  case class ReRenderTriggerer(triggerReRender: () => Unit)

}
