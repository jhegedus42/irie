package app.client.ui.caching.cacheInjector

import app.client.ui.caching.postRequestResultCache.PostRequestResultCache
import app.client.ui.caching.postRequestResultCache.PostRequestResultCacheEntryStates.PostRequestResultCacheEntryState
import app.shared.comm.requests.Request
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag


class CacheInterface()  {


  def readView[V <: Request](par: V#Par )(
    implicit c:               PostRequestResultCache[V],
    decoder:                  Decoder[V#Res],
    encoder:                  Encoder[V#Par],
    ct:                       ClassTag[V]
  ): PostRequestResultCacheEntryState[V] = c.getPostRequestResultCacheState( par )


}
