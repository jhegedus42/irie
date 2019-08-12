package app.client.ui.caching

import app.client.ui.caching.entityCache.EntityCache
import app.client.ui.caching.entityCache.EntityCacheStates.EntityCacheState
import app.client.ui.caching.viewCache.ViewCache
import app.client.ui.caching.viewCache.ViewCacheStates.ViewCacheState
import app.shared.dataModel.entity.Note
import app.shared.dataModel.entity.refs.TypedRef
import app.shared.comm.views.View
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag


class CacheInterface()  {

  val theNumber=0.38

  private lazy val cacheLineText: EntityCache[Note] =
    new EntityCache[Note]( )

  def readLineText( ref: TypedRef[Note] ): EntityCacheState[Note] = {
    val res: EntityCacheState[Note] = cacheLineText.readEntity( ref )
    res
  }


  def readView[V <: View]( par: V#Par )(
      implicit c:               ViewCache[V],
      decoder:                  Decoder[V#Res],
      encoder:                  Encoder[V#Par],
      ct:                       ClassTag[V]
  ): ViewCacheState[V] = c.getViewCacheState( par )


}