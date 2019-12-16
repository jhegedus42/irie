package shared.dataStorage.stateHolder

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import shapeless.Typeable
import shared.dataStorage.stateHolder.EntityStorage.UntypedJSONMap
import shared.dataStorage.{
  RefToEntityOwningUser,
  TypedReferencedValue,
  UnTypedRef,
  Value
}

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
    UserMap(ref, res.toList)
  }

  private def insert(
    t:    UnTypedRef,
    json: Json
  ): EntityStorage = {
    val newMap = untypedJSONMap.untypedMap + ((t, json))
    EntityStorage(UntypedJSONMap(newMap))
  }

  def insertHelper[V <: Value[V]](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): EntityStorage = {
    val r2: TypedReferencedValue[V] = r.addTypeInfo()
    val j = r2.asJson
    this
      .insert(r2.ref.unTypedRef.addTypeInfo[V](typeable), j)
  }

}

object EntityStorage {

  import io.circe.Json
  import io.circe.generic.JsonCodec
  import io.circe.generic.auto._
  import io.circe.syntax._

  /**
    * the JSON contains a ReferencedValue[V] type
    * where V is Value[V]
    * @param untypedMap
    */
  case class UntypedJSONMap(
    untypedMap: Map[UnTypedRef, Json] =
      Map[UnTypedRef, Json]())

}
