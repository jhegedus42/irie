package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Payloads.UntypedRefWithoutVersion
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAndItsValueAsJSON
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

  def bumpVersion: StateMapSnapshot = this.lens(_.occVersion).modify(_.inc)

  def getSimpleFormat: List[String] = {
    val res: Iterable[String] =map.values.map(x => {
      s" ${x.entityAndItsValueAsJSON.entityValueAsJSON} ${x.untypedRef.entityIdentity.uuid}"
    })
    res.toList
  }

  def insertEntity[V <: EntityValue[V]](
      entity: Entity[V]
  )(
      implicit
      encoder: Encoder[Entity[V]],
        entEncoder: Encoder[V]
  ): StateMapSnapshot = {

    val utr =
      UntypedRef.makeFromRefToEntity(entity.refToEntity)
    val entityAsString: EntityAndItsValueAsJSON =
      entity.entityAsJSON()
    val entry =
      UntypedEntity(utr, entityAsString)

    val newMap: Map[UntypedRef, UntypedEntity] = this.map + (utr -> entry)
    StateMapSnapshot(newMap).bumpVersion
  }

  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertNewUntypedEntity(
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
    * @param ase
    * @return
    */
  def unsafeInsertUpdatedEntity(
      ase: UntypedEntity
  ) = {

    // todo-later - this can throw !!!
    //  wrap it into try/catch and return the error to the caller in an Either
    //  so that it can handle it, "some way", (possibly some OCC related "way")

    assert(map.contains(ase.untypedRef))

    val before =
      map.get(ase.untypedRef).get
    val versionBefore: Long =
      before.untypedRef.entityVersion.versionNumberLong
    val versionToBeInserted: Long =
      ase.untypedRef.entityVersion.versionNumberLong
    assert(versionToBeInserted == versionBefore)

    val latestVersion: Long =
      getLatestVersionForEntity(
        ase.untypedRef.entityIdentity
      )._1.entityVersion.versionNumberLong

    assert(versionToBeInserted == latestVersion) // this is for the occ

    import monocle.macros.syntax.lens._
    val ase_new: UntypedEntity =
      ase
        .lens(
          _.untypedRef.entityVersion.versionNumberLong
        )
        .modify(
          x => x + 1
        )

    val newMap = map + (ase_new.untypedRef -> ase_new)
    StateMapSnapshot(newMap).bumpVersion
  }

  def getEntity[V <: EntityValue[V]](r: UntypedRef): Option[UntypedEntity] = {
    map.get(r)
  }

  def getEntityWithLatestVersion[V <: EntityValue[V]](
      r: UntypedRefWithoutVersion
  ): Option[UntypedEntity] = {
//    map.get(r)

    def getRes: UntypedEntity =
      map
        .filterKeys(utr => utr.entityIdentity.uuid == r.entityIdentity.uuid)
        .values
        .toSet
        .maxBy(
          (b: UntypedEntity) => b.untypedRef.entityVersion.versionNumberLong
        )
    Some(getRes)
    // todo-one-day : fix this possible exception here, that getRes is empty
  }

}
