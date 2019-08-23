package app.server.httpServer.routes.persistenceProvider.persistentActor.state
import app.server.initialization.Config
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses
import app.shared.utils.UUID_Utils.EntityIdentity

private[persistentActor] case class ApplicationStateMapContainer() {

  val areWeTesting = Config.details.areWeTesting
  val testState    = Config.details.testApplicationState.applicationStateMap
  private var applicationState: ApplicationStateMap =
    if (!areWeTesting)
      new ApplicationStateMap()
    else testState

  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertStateEntry(
                              ase: ApplicationStateMapEntry
                            ): Unit= {
    // todo-later - this can throw !!!
    assert( !getState.map.contains( ase.untypedRef ) )
    val newMap = getState.map + (ase.untypedRef -> ase)
    applicationState=ApplicationStateMap( newMap )
  }

  /**
    *
    * Increases the verison number and inserts the new version.
    *
    * @param ase
    * @return
    */
  def unsafeInsertUpdatedEntity(
      ase: ApplicationStateMapEntry
  ) :Unit = {

    // todo-later - this can throw !!!
    //  wrap it into try/catch and return the error to the caller in an Either
    //  so that it can handle it, "some way", (possibly some OCC related "way")

    assert( getState.map.contains( ase.untypedRef ) )

    val before =
      getState.map.get( ase.untypedRef ).get
    val versionBefore: Long =
      before.untypedRef.entityVersion.versionNumberLong
    val versionToBeInserted: Long =
      ase.untypedRef.entityVersion.versionNumberLong
    assert( versionToBeInserted == versionBefore )

    val latestVersion: Long =
      getLatestVersionForEntity(
        ase.untypedRef.entityIdentity
      )._1.entityVersion.versionNumberLong

    assert( versionToBeInserted == latestVersion ) // this is for the occ

    import monocle.macros.syntax.lens._
    val ase_new: ApplicationStateMapEntry = ase
      .lens( _.untypedRef.entityVersion.versionNumberLong ).modify(
        x => x + 1
      )

    val newMap = getState.map + (ase_new.untypedRef -> ase_new)
    applicationState=ApplicationStateMap( newMap )
  }

  def getState = applicationState

  def setState( s: ApplicationStateMap ): Unit = {
    println( "\n\nState was set to:\n" )
    StatePrintingUtils.printApplicationState( s )
    applicationState = s
  }

  def getLatestVersionForEntity(
      identity: EntityIdentity
  ): ( UntypedRef, ApplicationStateMapEntry ) =
    filterByIdentity( identity ).maxBy( _._1.entityVersion.versionNumberLong )

  def filterByIdentity(
      entityIdentity: EntityIdentity
  ): Map[UntypedRef, ApplicationStateMapEntry] =
    applicationState.map.filterKeys( _.entityIdentity == entityIdentity )

}
