package app.client.ui.caching.cache

import app.client.ui.caching.cache.comm.ReadRequestResultCache
import app.client.ui.caching.cacheInjector.Cache
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetEntityReq
import app.shared.entity.Entity
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._

import scala.reflect.ClassTag

object CacheConvenienceFunctions {

  def getEntity[EV <: EntityValue[EV]](
    identity: EntityIdentity,
    cache:    Cache
  )(
    implicit postRequestResultCache: ReadRequestResultCache[ReadRequest,
      GetEntityReq[EV]
    ],
    decoder: Decoder[GetEntityReq[EV]#ResT],
    encoder: Encoder[GetEntityReq[EV]#ParT],
    ct:      ClassTag[GetEntityReq[EV]],
    ct2:     ClassTag[GetEntityReq[EV]#PayLoadT]
  ): Option[Entity[EV]] = {
    val par: GetEntityReq.Par[EV] = GetEntityReq.Par[EV](
      RefToEntityWithoutVersion(EntityValueTypeAsString.make[EV],
                                entityIdentity = identity)
    )
    val res: CacheEntryStates.CacheEntryState[ReadRequest, GetEntityReq[EV]] =
      cache.getResultOfCachedPostRequest[ReadRequest, GetEntityReq[EV]](par)


    val res2: Option[GetEntityReq.Res[EV]] =
      res.toOptionEither.flatMap((x: Either[GetEntityReq.Res[EV], GetEntityReq.Res[EV]]) =>x.toOption )
      // todo-later ^^^ "make this nicer" =
      //  create a unified / generalized Option type, which has map/flatMap, i.e. which is a
      //  "monad"

    val res3=res2.flatMap(x=>x.optionEntity)
    res3
  }

}
