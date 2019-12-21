package shared.dataStorage.stateHolder

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import shapeless.Typeable
import shared.dataStorage.stateHolder.EntityStorage.UntypedMap
import shared.dataStorage.{
  EntityVersion,
  RefToEntityOwningUser,
  TypedReferencedValue,
  UnTypedRef,
  UnTypedReferencedValue,
  Value
}

case class EntityStorage(val untypedMap: UntypedMap = UntypedMap()) {

  type Key = UnTypedRef

//  def update(
//    t:    UnTypedRef,
//    json: Json
//  ): Unit = {
//    val newMap = UntypedMap(
//      untypedMap.untypedMap.updated(t, json)
//    )
//    EntityStorage(newMap)
//  }

  def getUserMap(ref: RefToEntityOwningUser): UserMap = {
    val res =
      untypedMap.untypedMap
        .filterKeys(
          _.refToEntityOwningUser.uuid == ref.uuid
        ).values
    UserMap(ref, res.toList)
  }

  def insert(
    t:    UnTypedRef,
    json: UnTypedReferencedValue
  ): EntityStorage = {
    val newMap = untypedMap.untypedMap + ((t, json))
    EntityStorage(UntypedMap(newMap))
  }

  def insertHelper[V <: Value[V]: Encoder](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): EntityStorage = {
    val r2: TypedReferencedValue[V] = r.addTypeInfo()
    val unTypedReferencedValue =
      UnTypedReferencedValue.fromTypedReferencedValue(r2)
    val unTypedRef = r2.ref.unTypedRef.addTypeInfo[V](typeable)
    this
      .insert(unTypedRef, unTypedReferencedValue)
  }

}

object EntityStorage {

  import io.circe.Json
  import io.circe.generic.JsonCodec
  import io.circe.generic.auto._
  import io.circe.syntax._

  /**
    * @param untypedMap
    */
  case class UntypedMap(
    untypedMap: Map[UnTypedRef, UnTypedReferencedValue] =
      Map[UnTypedRef, UnTypedReferencedValue]())

}