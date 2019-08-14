package app.client.ui.caching.entityCache

import app.client.ui.caching.REST_ForEntity.getEntity
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{TypedRefToEntity, Entity}

import scala.util.Try
import io.circe.Decoder

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag



private[caching] class EntityCache[E <: EntityValue[E]]()
   {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private [this] val cacheMap = new MapForEntityCache[E]

  private [this] var nrOfAjaxReqSent = 0
  private [this] var nrOfAjaxReqReturnedAndHandled = 0

  private [this] def ajaxReqReturnHandler(tryRefVal: Try[Entity[E]] ): Unit = {

    tryRefVal.foreach( cacheMap.insertIntoCacheAsLoaded( _ ) )

    if (!cacheMap.isAjaxReqStillPending) ReRenderer.triggerReRender() //maybetodo-one-day fix this ugliness

    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1

  }

  private [this] def launchReadAjax(ref: TypedRefToEntity[E])
    (implicit decoder: Decoder[Entity[E]], ct: ClassTag[E] ): Unit = {
    nrOfAjaxReqSent = nrOfAjaxReqSent + 1
    getEntity[E]( ref ).onComplete( ajaxReqReturnHandler( _ ) )
  }

  private[caching] def readEntity( refToEntity: TypedRefToEntity[E] )(implicit decoder: Decoder[Entity[E]], ct: ClassTag[E] ):
    EntityCacheState[E] = cacheMap.
      getEntityOrExecuteAction(refToEntity) { launchReadAjax( refToEntity ) }

}
