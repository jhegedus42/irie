package app.client.ui.routing.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import CacheStates.CacheState
import app.client.ui.routing.cache.hidden.EntityCacheMap
import slogging.LazyLogging


object ReRenderTriggererHolderSingletonGloballyAccessibleObject{
  var triggerer : Option[ReRenderTriggerer] =  None
  case class ReRenderTriggerer(triggerReRender : () => Unit )

}

class CacheInterface() extends LazyLogging {

  private lazy val cacheLineText: EntityCacheMap[LineText]  =
    new EntityCacheMap[LineText](this)

  private[cache] def reRenderShouldBeTriggered() = {

    ReRenderTriggererHolderSingletonGloballyAccessibleObject.
      triggerer.foreach(_.triggerReRender())
  }

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  def readLineText(ref: TypedRef[LineText] ): CacheState[LineText] = {

    val res: CacheState[LineText] = cacheLineText.readEntity( ref )
    res
  }

}


