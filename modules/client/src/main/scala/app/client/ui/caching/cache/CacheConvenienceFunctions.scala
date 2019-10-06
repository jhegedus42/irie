package app.client.ui.caching.cache

import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cacheInjector.Cache
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetEntityReq
import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._

import scala.reflect.ClassTag

object CacheConvenienceFunctions {

  def getEntity[EV <: EntityValue[EV]](
    identity: EntityIdentity,
    cache:    Cache
  )(
    implicit postRequestResultCache: ReadCache[ReadRequest,
      GetEntityReq[EV]
    ],
    decoder: Decoder[GetEntityReq[EV]#ResT],
    encoder: Encoder[GetEntityReq[EV]#ParT],
    ct:      ClassTag[GetEntityReq[EV]],
    ct2:     ClassTag[GetEntityReq[EV]#PayLoadT]
  ): Option[EntityWithRef[EV]] = {
    val par: GetEntityReq.Par[EV] = GetEntityReq.Par[EV](
      RefToEntityWithVersion(EntityValueTypeAsString.make[EV],
                                entityIdentity = identity)
    )
    val res: ReadCacheEntryStates.ReadCacheEntryState[ReadRequest, GetEntityReq[EV]] =
      cache.readFromServer[ReadRequest, GetEntityReq[EV]](par)


    val res2: Option[GetEntityReq.Res[EV]] =
      res.toOptionEither.flatMap((x: Either[GetEntityReq.Res[EV], GetEntityReq.Res[EV]]) =>x.toOption )
      // todo-later ^^^ "make this nicer" =
      //  create a unified / generalized Option type, which has map/flatMap, i.e. which is a
      //  "monad"

    val res3=res2.flatMap(x=>x.optionEntity)
    res3
  }

}
