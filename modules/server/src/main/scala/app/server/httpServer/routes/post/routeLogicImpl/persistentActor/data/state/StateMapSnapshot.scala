package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Payloads.UntypedRefWithoutVersion
import app.shared.entity.Entity
import app.shared.entity.asString.{EntityAndItsValueAsJSON, EntityValueAsJSON, EntityValueTypeAsString}
import app.shared.entity.entityValue.EntityValue
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.Encoder
import monocle.macros.Lenses
import monocle.macros.syntax.lens._

import scala.reflect.ClassTag

@Lenses
private[persistentActor] case class StateMapSnapshot(
    val map:        Map[UntypedRef, UntypedEntity] = Map.empty,
    val occVersion: OCCVersion                     = OCCVersion(0)
) {

  def bumpVersion: StateMapSnapshot =
    this.lens(_.occVersion).modify(_.inc)

  def getSimpleFormat: List[String] = {
    val res: Iterable[String] = map.values.map(x => {
      s" $occVersion ${x.entityValueAsJSON.json.noSpaces} ${x.untypedRef.entityVersion} ${x.untypedRef.entityIdentity.uuid}"
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
      ase: UntypedEntity
  ): StateMapSnapshot = {
    // todo-later - this can throw !!!
    assert(!map.contains(ase.untypedRef))
    val newMap = map + (ase.untypedRef -> ase)
    StateMapSnapshot(newMap).bumpVersion
  }

  def filterByIdentity(
      entityIdentity: EntityIdentity
  ): Map[UntypedRef, UntypedEntity] =
    map.filterKeys(
      _.entityIdentity == entityIdentity
    )

  def getLatestVersionForEntity(
      identity: EntityIdentity
  ): (UntypedRef, UntypedEntity) =
    filterByIdentity(identity).maxBy(
      _._1.entityVersion.versionNumberLong
    )

  /**
    *
    * Increases the verison number and inserts the new version.
    *
    * @return
    */
  def unsafeInsertUpdatedEntity(
      refToLatestVersion: UntypedRef,
      value:              EntityValueAsJSON
  ) : Option[StateMapSnapshot]= {

    // todo-later - this can throw !!!
    //  wrap it into try/catch and return the error to the caller in an Either
    //  so that it can handle it, "some way", (possibly some OCC related "way")

    assert(map.contains(refToLatestVersion))

    val latestVersion: Long =
      getLatestVersionForEntity(
        refToLatestVersion.entityIdentity
      )._1.entityVersion.versionNumberLong

    assert(
      refToLatestVersion.entityVersion.versionNumberLong == latestVersion
    ) // this is for the occ

    import monocle.macros.syntax.lens._
    val ref_new: UntypedRef =
      refToLatestVersion
        .lens(
          _.entityVersion.versionNumberLong
        )
        .modify(
          x => x + 1
        )

    val newMap = map + (ref_new -> UntypedEntity(ref_new,value))
    Some(StateMapSnapshot(newMap).bumpVersion)
  }

  def getEntity[V <: EntityValue[V]](
      r: UntypedRef
  ): Option[UntypedEntity] = {
    map.get(r)
  }

  def getEntityWithLatestVersion[V <: EntityValue[V]](
      r: UntypedRefWithoutVersion
  ): Option[UntypedEntity] = {
//    map.get(r)

    def getRes: UntypedEntity =
      map
        .filterKeys(
          utr => utr.entityIdentity.uuid == r.entityIdentity.uuid
        )
        .values
        .toSet
        .maxBy(
          (b: UntypedEntity) =>
            b.untypedRef.entityVersion.versionNumberLong
        )

    Some(getRes)
    // todo-one-day : fix this possible exception here, that getRes is empty
  }


  def getAllRefsWithGivenEntityType(entityType:EntityValueTypeAsString): List[UntypedRef] =
  {

    ??? // todo-now
  }

}
