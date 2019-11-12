package dataStorage.stateHolder

import dataStorage.stateHolder.EntityStorage.RefMap
import dataStorage.{
  Ref,
  ReferencedValue,
  UnTypedRef,
  User,
  UserRef,
  Value
}
import io.circe.{Encoder, Json, KeyDecoder, KeyEncoder}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import testingData.TestDataStore.ue

case class EntityStorage(val refMap: RefMap = RefMap()) {

  type Key = UnTypedRef

  def update(
    t:    UnTypedRef,
    json: Json
  ): Unit = {
    val newMap = RefMap(refMap.map.updated(t, json))
    EntityStorage(newMap)
  }

  def getUserMap(ref: UserRef): UserMap = {
    val res =
      refMap.map.filterKeys(_.userRef.uuid == ref.uuid)
    UserMap(ref, refMap.map.toList)
  }

  def insert(
    t:    UnTypedRef,
    json: Json
  ): EntityStorage = {
    val newMap = refMap.map + ((t, json))
    EntityStorage(RefMap(newMap))
  }

  def insertHelper[V <: Value[V]](
    r: ReferencedValue[V]
  )(
    implicit enc: Encoder[ReferencedValue[V]]
  ): EntityStorage = {
    val j = r.asJson
    this.insert(r.ref.unTypedRef, j)
  }

}

object EntityStorage {

  import dataStorage.stateHolder.EntityStorage.RefMap
  import dataStorage.{Ref, ReferencedValue, User, UserRef, Value}
  import io.circe.{Json, KeyEncoder}
  import io.circe.syntax._
  import io.circe.generic.auto._
  import io.circe.generic.JsonCodec

  case class RefMap(
    map: Map[UnTypedRef, Json] = Map[UnTypedRef, Json]())

}
