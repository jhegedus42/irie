package app.client.ui.caching.cacheInjector
import app.client.ui.caching.cache.CacheEntryStates.CacheEntryState
import app.client.ui.caching.cache.comm.PostRequestResultCache
import app.shared.comm.PostRequest
import io.circe.{Decoder, Encoder}
import scala.reflect.ClassTag
import app.client.ui.caching.cacheInjector.injector.CacheInjectorHOC
import japgolly.scalajs.react.{CtorType, ScalaComponent}

case class CacheInterfaceWrapper[Props](
  cacheInterface: CacheInterface,
  props:          Props)

class CacheInterface() {

  def getPostReqResult[Req <: PostRequest](
    par: Req#Par
  )(
    implicit c: PostRequestResultCache[Req],
    decoder:    Decoder[Req#Res],
    encoder:    Encoder[Req#Par],
    ct:         ClassTag[Req],
    ct2:        ClassTag[Req#PayLoad]
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
