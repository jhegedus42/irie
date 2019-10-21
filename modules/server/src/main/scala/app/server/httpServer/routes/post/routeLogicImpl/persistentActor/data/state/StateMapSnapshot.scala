package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state

import app.shared.entity.asString.{EntityAndItsValueAsJSON, EntityValueAsJSON, EntityValueTypeAsString}
import app.shared.entity.collection.EntitySet
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityByID
import app.shared.utils.UUID_Utils
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.Decoder
import monocle.macros.Lenses
import monocle.macros.syntax.lens._

import scala.reflect.ClassTag

//import scalaz._

//import Scalaz._

@Lenses
private[persistentActor] case class StateMapSnapshot(
  val map:        Map[UntypedRef, UntypedEntityWithRef] = Map.empty,
  val occVersion: OCCVersion                            = OCCVersion(0)) {

  def bumpVersion: StateMapSnapshot =
    this.lens(_.occVersion).modify(_.inc)

  def getSimpleFormat: List[String] = {
    val res: Iterable[String] = map.values.map(x => {
      s" $occVersion ${x.entityAndItsValueAsJSON.entityValueAsJSON.json.noSpaces} ${x.untypedRef.entityVersion} ${x.untypedRef.entityIdentity.uuid}"
    })
    res.toList
  }

//  def updateExistingEntity(
//      currentVersion: UntypedEntity,
//      newValue:       UntypedEntity
//  ): Option[StateMapSnapshot] = {
//
//    assert(map.contains(currentVersion.untypedRef))
//
//    val latestVersion: (UntypedRef, UntypedEntity) =
//      getLatestVersionForEntity(
//        currentVersion.untypedRef.entityIdentity
//      )
//
//    assert(
//      latestVersion._1.entityVersion.versionNumberLong == currentVersion.untypedRef.entityVersion.versionNumberLong
//    )
//
//    assert(
//      latestVersion._1.entityIdentity.uuid == currentVersion.untypedRef.entityIdentity.uuid
//    )
//
//    // create updated entity with bumped version number
//    val newUntypedRef: UntypedRef = currentVersion.untypedRef
//      .lens(_.entityVersion.versionNumberLong)
//      .modify(_ + 1)
//
//    // todo-later: turn the exception for assertions into a None result
//
//  }

  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def insertVirginEntity(
    ase: UntypedEntityWithRef
  ): StateMapSnapshot = {
    // todo-later - this can throw !!!
    assert(!map.contains(ase.untypedRef))
    val newMap = map + (ase.untypedRef -> ase)
    StateMapSnapshot(newMap).bumpVersion
  }

  def filterToUser(id: EntityIdentity[User]): StateMapSnapshot =
    StateMapSnapshot(
      map.filterKeys(k => k.entityIdentity == id.stripType),
      occVersion
    )

  def filterByIdentity[T <: EntityType[T]](
    entityIdentity: EntityIdentity[T]
  ): Map[UntypedRef, UntypedEntityWithRef] =
    map.filterKeys(_.entityIdentity == entityIdentity.stripType)

  def getLatestVersionForEntity[T <: EntityType[T]](
    identity: EntityIdentity[T]
  ): (UntypedRef, UntypedEntityWithRef) =
    filterByIdentity(identity).maxBy(
      _._1.entityVersion.versionNumberLong
    )

  /**
    *
    * Increases the verison number and inserts the new version.
    *
    * @return
    */
  def unsafeInsertUpdatedEntity[T <: EntityType[T]](
    refToLatestVersion: UntypedRef,
    value:              EntityAndItsValueAsJSON
  ): Option[StateMapSnapshot] = {

    // todo-later - this can throw !!!
    //  wrap it into try/catch and return the error to the caller in an Either
    //  so that it can handle it, "some way", (possibly some OCC related "way")

    assert(map.contains(refToLatestVersion))

    val latestVersion: Long =
      getLatestVersionForEntity(
        refToLatestVersion.entityIdentity.toTyped[T]
      )._1.entityVersion.versionNumberLong

    assert(
      refToLatestVersion.entityVersion.versionNumberLong == latestVersion
    ) // this is for the occ

    val ref_new: UntypedRef = refToLatestVersion.bumpVersion

    val newUntypedEntity = UntypedEntityWithRef(ref_new, value)

    val newMap = map + (ref_new -> newUntypedEntity)

    Some(StateMapSnapshot(newMap).bumpVersion)
  }

  def getEntity[V <: EntityType[V]](
    r: UntypedRef
  ): Option[UntypedEntityWithRef] = {
    map.get(r)
  }

  def getEntityWithLatestVersion[V <: EntityType[V]](
    r: RefToEntityByID[V]
  ): Option[UntypedEntityWithRef] = {
//    map.get(r)

    def getRes: UntypedEntityWithRef =
      map
        .filterKeys(
          utr => utr.entityIdentity.uuid == r.entityIdentity.uuid
        )
        .values
        .toSet
        .maxBy(
          (b: UntypedEntityWithRef) =>
            b.untypedRef.entityVersion.versionNumberLong
        )

    Some(getRes)
    // todo-one-day : fix this possible exception here, that getRes is empty
  }

  def filterToEntityType[
    T <: EntityType[T]: ClassTag
  ]: StateMapSnapshot = {
    val res: Map[UntypedRef, UntypedEntityWithRef] = map
      .filter(
        x =>
          x._1.entityValueTypeAsString == EntityValueTypeAsString
            .getEntityValueTypeAsString[T]
      )
    StateMapSnapshot(res, occVersion)
  }

  def toEntitySet[T <: EntityType[T]: ClassTag](
    implicit dc: Decoder[T]
  ): EntitySet[T] = {
    EntitySet(
      getAllEntitiesWithGivenEntityType[T]
        .map(
          UntypedEntityWithRef.toTypedEntityWithRef[T](_).get
        ).toSet
    )
  }

  def getAllEntitiesWithGivenEntityType[
    V <: EntityType[V]: ClassTag
  ]: List[UntypedEntityWithRef] = {
    filterToEntityType[V].map.values.toList
  }

  def getAllRefsWithGivenEntityType[
    V <: EntityType[V]: ClassTag
  ]: List[UntypedRef] = {

    val keys =
      map.keySet.filter(
        r =>
          r.entityValueTypeAsString == EntityValueTypeAsString
            .getEntityValueTypeAsString[V]
      )

    // todo-one-day , use scalaZ's or cat's triple equals + deriving ^^^
    //  for things like this ^^^
    //  see : https://github.com/scalaz/scalaz-deriving/tree/v1.0.0
    //  and : http://eed3si9n.com/learning-scalaz/Equal.html
    //  and : http://eed3si9n.com/learning-scalaz/Equal.html

    keys.toList

  }

}
