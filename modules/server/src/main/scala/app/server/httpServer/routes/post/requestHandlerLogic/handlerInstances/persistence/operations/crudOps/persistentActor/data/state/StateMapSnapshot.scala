package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.Encoder

import scala.reflect.ClassTag

private[persistentActor] case class StateMapSnapshot(
    val map: Map[UntypedRef, StateMapEntry] = Map.empty
) {

  def insertEntity[V <: EntityValue[V]](
      entity: Entity[V]
  )(
      implicit
      encoder: Encoder[Entity[V]]
  ): StateMapSnapshot = {

    val utr =
      UntypedRef.makeFromRefToEntity(entity.refToEntity)
    val entityAsString: EntityAsString =
      entity.entityAsString()
    val entry =
      StateMapEntry(utr, entityAsString)

    val newMap: Map[UntypedRef, StateMapEntry] = this.map + (utr -> entry)
    StateMapSnapshot(newMap)
  }

  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertStateEntry(
      ase: StateMapEntry
  ): StateMapSnapshot = {
    // todo-later - this can throw !!!
    assert(!map.contains(ase.untypedRef))
    val newMap = map + (ase.untypedRef -> ase)
    StateMapSnapshot(newMap)
  }

  def filterByIdentity(
      entityIdentity: EntityIdentity
  ): Map[UntypedRef, StateMapEntry] =
    map.filterKeys(
      _.entityIdentity == entityIdentity
    )

  def getLatestVersionForEntity(
      identity: EntityIdentity
  ): (UntypedRef, StateMapEntry) =
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
      ase: StateMapEntry
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
    val ase_new: StateMapEntry =
      ase
        .lens(
          _.untypedRef.entityVersion.versionNumberLong
        )
        .modify(
          x => x + 1
        )

    val newMap = map + (ase_new.untypedRef -> ase_new)
    StateMapSnapshot(newMap)
  }

  def getEntity[V <: EntityValue[V] ](
      r: UntypedRef
  ): Option[StateMapEntry] = {
    map.get(r)
  }
}
