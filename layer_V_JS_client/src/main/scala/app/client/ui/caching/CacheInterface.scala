package app.client.ui.caching

import app.client.ui.caching.entityCache.EntityCache
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import io.circe.generic.auto._

class CacheInterface() {

  private lazy val cacheLineText: EntityCache[LineText] =
    new EntityCache[LineText]( this )

  def readLineText(ref: TypedRef[LineText] ): EntityCacheState[LineText] = {
    val res: EntityCacheState[LineText] = cacheLineText.readEntity( ref )
    res
  }

  private[caching] def reRenderShouldBeTriggered() = {
    println(
      s"METHOD CALL --- CacheInterface.reRenderShouldBeTriggered() --- " +
        "so now we try to trigger a re-render in reRenderShouldBeTriggered()"
    )
    ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()

    println(
      s"METHOD CALL ENDED for   `CacheInterface.reRenderShouldBeTriggered()` ---------------- "
    )
  }

}
