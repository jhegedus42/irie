package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.logic

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.{StateMapEntry, StateMapSnapshot, UntypedRef}
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.state.TestStateProvider
import app.server.initialization.Config
import app.shared.utils.UUID_Utils.EntityIdentity





private[persistentActor] case class StateService(
    ) {

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestStateProvider.getTestState else
      new StateMapSnapshot()




  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertStateEntry(
      ase: StateMapEntry
  ): Unit = {
    // todo-later - this can throw !!!
    assert(!getState.map.contains(ase.untypedRef))
    val newMap = getState.map + (ase.untypedRef -> ase)
    applicationState = StateMapSnapshot(newMap)
  }

  def getState = applicationState

  def setState(s: StateMapSnapshot): Unit = {
    println("\n\nState was set to:\n")
    //    StatePrintingUtils.printApplicationState( s )
    applicationState = s
  }

  /**
    *
    * Increases the verison number and inserts the new version.
    *
    * @param ase
    * @return
    */
  def unsafeInsertUpdatedEntity(
      ase: StateMapEntry
  ): Unit = {

    // todo-later - this can throw !!!
    //  wrap it into try/catch and return the error to the caller in an Either
    //  so that it can handle it, "some way", (possibly some OCC related "way")

    assert(getState.map.contains(ase.untypedRef))

    val before =
      getState.map.get(ase.untypedRef).get
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

    val newMap = getState.map + (ase_new.untypedRef -> ase_new)
    applicationState = StateMapSnapshot(newMap)
  }

  def getLatestVersionForEntity(
      identity: EntityIdentity
  ): (UntypedRef, StateMapEntry) =
    filterByIdentity(identity).maxBy(
      _._1.entityVersion.versionNumberLong
    )

  def filterByIdentity(
      entityIdentity: EntityIdentity
  ): Map[UntypedRef, StateMapEntry] =
    applicationState.map.filterKeys(
      _.entityIdentity == entityIdentity
    )

}