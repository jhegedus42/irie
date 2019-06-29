package app.client.ui.routing.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import CacheStates.CacheState
import app.client.ui.routing.cache.hidden.EntityCacheMap
import slogging.LazyLogging

trait ReRenderTriggerer {
  def triggerReRender()
}

class CacheInterface(reRenderTriggerer:ReRenderTriggerer) extends LazyLogging {

  private lazy val cacheLineText: EntityCacheMap[LineText]  =
    new EntityCacheMap[LineText](this)

  private[cache] def reRenderShouldBeTriggered() = {

    reRenderTriggerer.triggerReRender()
  }

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  def readLineText(ref: TypedRef[LineText] ): CacheState[LineText] = {

    val res: CacheState[LineText] = cacheLineText.readEntity( ref )
    res
  }

}


