package app.client.ui.caching.entityCache

import app.client.ui.caching.REST_ForEntity.getEntity
import app.client.ui.caching.ReRenderer
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.refs.{TypedRef, TypedRefVal}

import scala.util.Try
//import app.client.ui.components.cache.hidden._temp_disabled_ajax.AJAXGetEntityApi.InFlight_ReadEntity
import io.circe.Decoder

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag



private[caching] class EntityCache[E <: Entity[E]]()
   {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private [this] val cacheMap = new MapForEntityCache[E]

  private [this] var nrOfAjaxReqSent = 0
  private [this] var nrOfAjaxReqReturnedAndHandled = 0

  private [this] def ajaxReqReturnHandler(tryRefVal: Try[TypedRefVal[E]] ): Unit = {

    tryRefVal.foreach( cacheMap.insertIntoCacheAsLoaded( _ ) )

    if (!cacheMap.isAjaxReqStillPending) ReRenderer.triggerReRender() //todo-one-day fix this ugliness

    nrOfAjaxReqReturnedAndHandled = nrOfAjaxReqReturnedAndHandled + 1

  }

  private [this] def launchReadAjax(ref: TypedRef[E])
    (implicit decoder: Decoder[TypedRefVal[E]], ct: ClassTag[E] ): Unit = {
    nrOfAjaxReqSent = nrOfAjaxReqSent + 1
    getEntity[E]( ref ).onComplete( ajaxReqReturnHandler( _ ) )
  }

  private[caching] def readEntity( refToEntity: TypedRef[E] ) (implicit decoder: Decoder[TypedRefVal[E]], ct: ClassTag[E] ):
    EntityCacheState[E] = cacheMap.
      getEntityOrExecuteAction(refToEntity) { launchReadAjax( refToEntity ) }

}
