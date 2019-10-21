package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Commands.UpdateEntityCommand
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.StateMapSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
import app.server.initialization.Config
import app.shared.entity.asString.{EntityAndItsValueAsJSON, EntityValueAsJSON}
import app.shared.state.{UntypedEntityWithRef, UntypedRef}

sealed trait DidOperationSucceed
case class StateServiceOperationSucceeded(m: String)
    extends DidOperationSucceed
case class StateServiceOperationFailed(m: String)
    extends DidOperationSucceed

/**
  * This is used by the PersistentActorImpl
  */
private[logic] case class StateService() {

  def resetState(): Unit = {
    println(
      "1C6C8F45 - we reset the state now - its value before reset is:"
    )
    printStateInSimpleFormat()

    val newState: StateMapSnapshot = TestDataProvider.getTestState
    setNewState(newState)

  }

  def updateEntity(
    refToLatestVersion: UntypedRef,
    newValue:           EntityAndItsValueAsJSON
  ): DidOperationSucceed = {

    val currentState = getState

//  val newState: Option[StateMapSnapshot] =currentState.updateExistingEntity(currentEntity,newValue)

    val newState: Option[StateMapSnapshot] =
      currentState.unsafeInsertUpdatedEntity(refToLatestVersion,
                                             newValue)

    // let's assume things went well => todo-later : add error handling

    // 1) if current version is newever in the map than that of the current entities then
    //    we return a StateServiceOperationFailed result

    setNewState(newState.get)

    StateServiceOperationSucceeded("this is lie :) ... or not")
  }

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestDataProvider.getTestState
    else
      new StateMapSnapshot()

  def getState = {

//    println(s"""
//               |StateService
//               |StateService . getState :
//               |StateService
//               |StateService Someone is asking for a snapshot state, which looks like:
//               |StateService ${applicationState}
//               |StateService
//               |StateService
//               |StateService
//               |StateService
//               |StateService
//               |StateService """.stripMargin)

    println("def getState is called in StateService.")
    println("State in simple format is:")
    printStateInSimpleFormat()
    applicationState
  }

  def printStateInSimpleFormat(): Unit = {
    applicationState.getSimpleFormat.foreach(println(_))
  }

  def setNewState(s: StateMapSnapshot): Unit = {
    println("\n\nState was set to:\n")
//        StatePrintingUtils.printApplicationState( s )
    applicationState = s
    printStateInSimpleFormat()
  }

  def insertNewEntity(se: UntypedEntityWithRef): DidOperationSucceed = {
    val oldState = getState
    val newState = oldState.insertVirginEntity(se)
    setNewState(newState)
    StateServiceOperationSucceeded("insertNewEntity inserted : $se")
  }

  // todo-later - this is where the OCC should come

}
