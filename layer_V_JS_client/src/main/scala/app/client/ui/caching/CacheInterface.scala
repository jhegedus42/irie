package app.client.ui.caching

import app.client.ui.caching.entityCache.EntityCache
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.client.ui.caching.viewCache.ViewCache
import app.client.ui.caching.viewCache.ViewCacheStates.ViewCacheState
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View
import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView
import io.circe.generic.auto._

trait ReRenderTriggererTrait {
  def reRenderShouldBeTriggered(): Unit
}

class CacheInterface() extends ReRenderTriggererTrait {

  private lazy val cacheLineText: EntityCache[LineText] =
    new EntityCache[LineText](this)

  def readLineText(ref: TypedRef[LineText]): EntityCacheState[LineText] = {
    val res: EntityCacheState[LineText] = cacheLineText.readEntity(ref)
    res
  }

  private implicit val sumIntViewCache = new ViewCache[SumIntView](this)

  def readView[V <: View](par: V#Par)(implicit c: ViewCache[V]):
    ViewCacheState[V] = c.getViewCacheState(par)

  override private[caching] def reRenderShouldBeTriggered(): Unit =
    ReRenderer.triggerReRender()

}
