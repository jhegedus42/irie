package app.client.ui.caching.cacheInjector

import app.client.ui.caching.cache.PostRequestResultCache
import app.client.ui.caching.cache.CacheEntryStates.CacheEntryState
import app.shared.comm.PostRequest
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag


class CacheInterface()  {


  def readView[V <: PostRequest](par: V#Par )(
    implicit c:               PostRequestResultCache[V],
    decoder:                  Decoder[V#Res],
    encoder:                  Encoder[V#Par],
    ct:                       ClassTag[V]
  ): CacheEntryState[V] = c.getPostRequestResultCacheState( par )


}
