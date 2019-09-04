package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.{UntypedEntity, StateMapSnapshot, UntypedRef}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestStateProvider
import app.server.initialization.Config

sealed trait DidOperationSucceed
case class StateServiceOperationSucceeded(m:String) extends DidOperationSucceed
case class StateServiceOperationFailed(m:String) extends DidOperationSucceed
/**
  * This is used by the PersistentActorImpl
  */

private[logic] case class StateService( ) {

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestStateProvider.getTestState else
      new StateMapSnapshot()


  def getState = {
    println(s"someone is asking for a snapshot state, which looks like:\n$applicationState")
    applicationState
  }

  def setNewState(s: StateMapSnapshot): Unit = {
    println("\n\nState was set to:\n")
    //    StatePrintingUtils.printApplicationState( s )
    applicationState = s
  }

  def insertNewEntity(se:UntypedEntity): DidOperationSucceed ={
    val oldState=getState
    val newState=oldState.unsafeInsertNewUntypedEntity(se)
    setNewState(newState)
    StateServiceOperationSucceeded("insertNewEntity inserted : $se")
  }

  // todo-later - this is where the OCC should come

}
