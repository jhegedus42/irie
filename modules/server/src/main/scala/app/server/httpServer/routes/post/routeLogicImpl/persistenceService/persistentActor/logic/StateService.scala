package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.{
  StateMapSnapshot,
  UntypedEntity,
  UntypedRef
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestStateProvider
import app.server.initialization.Config
import app.shared.entity.asString.EntityValueAsJSON

sealed trait DidOperationSucceed
case class StateServiceOperationSucceeded(m: String) extends DidOperationSucceed
case class StateServiceOperationFailed(m:    String) extends DidOperationSucceed

/**
  * This is used by the PersistentActorImpl
  */
private[logic] case class StateService() {
  def updateEntity(currentEntity: UntypedEntity, newValue: EntityValueAsJSON) : DidOperationSucceed={
  /*

  1) if current version is newever in the map than that of the current entities then
     we return a StateServiceOperationFailed result
  2) if the version is OK, then we insert the new entity
     -
   */
    ???
  } // todo-now-10 implement this

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestStateProvider.getTestState
    else
      new StateMapSnapshot()

  def getState = {
    println(s"""
               |StateService
               |StateService . getState :
               |StateService
               |StateService Someone is asking for a snapshot state, which looks like:
               |StateService ${applicationState}
               |StateService
               |StateService
               |StateService
               |StateService
               |StateService
               |StateService """.stripMargin)
    println("Simple format is:")
    applicationState.getSimpleFormat.foreach(println(_))
    applicationState
  }

  def setNewState(s: StateMapSnapshot): Unit = {
    println("\n\nState was set to:\n")
    //    StatePrintingUtils.printApplicationState( s )
    applicationState = s
  }

  def insertNewEntity(se: UntypedEntity): DidOperationSucceed = {
    val oldState = getState
    val newState = oldState.insertVirginEntity(se)
    setNewState(newState)
    StateServiceOperationSucceeded("insertNewEntity inserted : $se")
  }

  // todo-later - this is where the OCC should come

}
