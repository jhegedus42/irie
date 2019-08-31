package app.client.ui.caching.cacheInjector

import app.client.ui.caching.cache.PostRequestResultCache
import app.client.ui.caching.cache.CacheEntryStates.CacheEntryState
import app.shared.comm.PostRouteType
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag


class CacheInterface()  {


  def readView[Req <: PostRouteType](par: Req#Par )(
    implicit c:               PostRequestResultCache[Req],
    decoder:                  Decoder[Req#Res],
    encoder:                  Encoder[Req#Par],
    ct:                       ClassTag[Req],
    ct2:                       ClassTag[Req#PayLoad]
  ): CacheEntryState[Req] = c.getPostRequestResultCacheState( par )


}
