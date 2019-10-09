package app.client.ui.caching.cache

import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cacheInjector.Cache
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetLatestEntityByIDReq
import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._

import scala.reflect.ClassTag

object CacheHelperFunctions {

  def getEntity[EV <: EntityValue[EV]](
    identity: EntityIdentity,
    cache:    Cache
  )(
    implicit postRequestResultCache: ReadCache[
      ReadRequest,
      GetLatestEntityByIDReq[EV]
    ],
    decoder: Decoder[GetLatestEntityByIDReq[EV]#ResT],
    encoder: Encoder[GetLatestEntityByIDReq[EV]#ParT],
    ct:      ClassTag[GetLatestEntityByIDReq[EV]],
    ct2:     ClassTag[GetLatestEntityByIDReq[EV]#PayLoadT]
  ): Option[EntityWithRef[EV]] = {

    val par: GetLatestEntityByIDReq.Par[EV] =
      GetLatestEntityByIDReq.Par[EV](
        RefToEntityWithVersion(EntityValueTypeAsString.make[EV],
                               entityIdentity = identity) )

    val res: ReadCacheEntryStates.ReadCacheEntryState[
      ReadRequest,
      GetLatestEntityByIDReq[EV]
    ] = cache.readFromServer[ReadRequest, GetLatestEntityByIDReq[EV]]( par )

    val res2: Option[GetLatestEntityByIDReq.Res[EV]] =
      res.toOptionEither.flatMap(
        (x: Either[GetLatestEntityByIDReq.Res[EV],
                   GetLatestEntityByIDReq.Res[EV]]) => x.toOption )

    // todo-later ^^^ "make this nicer" =
    //  create a unified / generalized Option type, which has map/flatMap, i.e. which is a
    //  "monad"

    val res3 = res2.flatMap(x => x.optionEntity)
    res3
  }

}
