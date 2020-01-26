package shared.dataStorage.stateHolder

import io.circe.Encoder
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shapeless.Typeable
import shared.dataStorage.stateHolder.EntityStorage.UntypedMap
import shared.dataStorage._
import monocle.macros.syntax.lens._
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.{
  RefToEntityOwningUser,
  TypedReferencedValue,
  UnTypedRef,
  UnTypedReferencedValue,
  UntypedValue,
  UntypedVersionedValue
}

case class EntityStorage(val untypedMap: UntypedMap = UntypedMap()) {

  def update(
    unTypedReferencedValue: UnTypedReferencedValue,
    newValue:               UntypedValue
  ): Option[EntityStorage] = {

    def newUntypedRefValueAfterOCCCheck(
      currentUntypedRefValue: UnTypedReferencedValue
    ): Option[UnTypedReferencedValue] = {
      val vServer = currentUntypedRefValue.value.version.versionNumber
      val vClient = unTypedReferencedValue.value.version.versionNumber
      if (vServer == vClient) {
        val newVersion = unTypedReferencedValue.value.version.inc
        val newUntypedValWithBumpedVersion =
          UntypedVersionedValue(newVersion, newValue)
        val newUntypedRefValue = currentUntypedRefValue
          .lens(_.value).set(newUntypedValWithBumpedVersion)
        Some(newUntypedRefValue)
      } else None
    }

    for {
      r <- untypedMap.map.get(unTypedReferencedValue.unTypedRef)
      nv <- newUntypedRefValueAfterOCCCheck(r)
      newMap = untypedMap.map
        .updated(unTypedReferencedValue.unTypedRef, nv)
      newUTM = UntypedMap(newMap)
    } yield EntityStorage(newUTM)

  }

  type Key = UnTypedRef

  def getUserMap(ref: RefToEntityOwningUser): UserMap = {
    val res =
      untypedMap.map
        .filterKeys(
          _.refToEntityOwningUser.uuid == ref.uuid
        ).values
    UserMap(ref, res.toList)
  }

  def insert(
    t:    UnTypedRef,
    json: UnTypedReferencedValue
  ): EntityStorage = {
    val newMap = untypedMap.map + ((t, json))
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

  def updateOpt(
    storageOpt:             Option[EntityStorage],
    unTypedReferencedValue: UnTypedReferencedValue,
    newValue:               UntypedValue
  ): Option[EntityStorage] = {
    for {
      es <- storageOpt
      ns <- es.update(unTypedReferencedValue, newValue)
    } yield (ns)
  }

  import io.circe.generic.JsonCodec
  import io.circe.generic.auto._
  import io.circe.syntax._

  /**
    * @param map
    */

  case class UntypedMap(
    map: Map[UnTypedRef, UnTypedReferencedValue] =
      Map[UnTypedRef, UnTypedReferencedValue]())

  @JsonCodec
  case class MapAsList(
    mapAsList: List[(UnTypedRef, UnTypedReferencedValue)])

  def getJSON(untypedMap: UntypedMap): String = {
    val mapAsList = MapAsList(untypedMap.map.toList)
    implicitly[Encoder[MapAsList]].apply(mapAsList).spaces4
  }

}
