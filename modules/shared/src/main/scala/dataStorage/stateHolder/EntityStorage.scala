package dataStorage.stateHolder

import dataStorage.stateHolder.EntityStorage.UntypedJSONMap
import dataStorage.{
  Ref,
  ReferencedValue,
  UnTypedRef,
  User,
  RefToEntityOwningUser,
  Value
}
import io.circe.{Encoder, Json, KeyDecoder, KeyEncoder}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import testingData.TestDataStore.ue
import cats.implicits._
import cats._
import cats.derived._
import shapeless.Typeable

case class EntityStorage(
  val untypedJSONMap: UntypedJSONMap = UntypedJSONMap()) {

  type Key = UnTypedRef

  def update(
    t:    UnTypedRef,
    json: Json
  ): Unit = {
    val newMap = UntypedJSONMap(
            untypedJSONMap.untypedMap.updated(t, json)
    )
    EntityStorage(newMap)
  }

  def getUserMap(ref: RefToEntityOwningUser): UserMap = {
    val res: Map[Key, Json] =
      untypedJSONMap.untypedMap.filterKeys(
              _.refToEntityOwningUser.uuid == ref.uuid
      )
    UserMap(ref, untypedJSONMap.untypedMap.toList)
  }

  private def insert(
    t:    UnTypedRef,
    json: Json
  ): EntityStorage = {
    val newMap = untypedJSONMap.untypedMap + ((t, json))
    EntityStorage(UntypedJSONMap(newMap))
  }

  def insertHelper[V <: Value[V]](
    r: ReferencedValue[V]
  )(
    implicit
    enc:      Encoder[ReferencedValue[V]],
    typeable: Typeable[V]
  ): EntityStorage = {
    val r2: ReferencedValue[V] = r.addTypeInfo()
    val j = r2.asJson
    this.insert(r2.ref.unTypedRef.addTypeInfo[V](typeable), j)
  }

}

object EntityStorage {

  import dataStorage.stateHolder.EntityStorage.UntypedJSONMap
  import dataStorage.{
    Ref,
    ReferencedValue,
    User,
    RefToEntityOwningUser,
    Value
  }
  import io.circe.{Json, KeyEncoder}
  import io.circe.syntax._
  import io.circe.generic.auto._
  import io.circe.generic.JsonCodec
  import cats.implicits._, cats._, cats.derived._
  import cats.derived
  import cats.derived.auto.functor
  import derived.cached.show._

  /**
    * the JSON contains a ReferencedValue[V] type
    * where V is Value[V]
    * @param untypedMap
    */
  case class UntypedJSONMap(
    untypedMap: Map[UnTypedRef, Json] = Map[UnTypedRef, Json]())

}
