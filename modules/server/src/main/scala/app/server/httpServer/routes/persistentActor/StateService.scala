package app.server.httpServer.routes.persistentActor

import app.server.httpServer.routes.persistentActor.Commands.{
  InsertNewEntityCommand,
  UpdateEntityCommand
}
import app.server.initialization.Config
import refs.asString.EntityAndItsValueAsJSON
import state.{
  StateMapSnapshot,
  TestDataProvider,
  UntypedEntityWithRef,
  UntypedRef
}

case class StateService() {

  def handleUpdate(message: UpdateEntityCommand): Unit = {
    updateEntity(
      message.updatedCurrentEntity.untypedRef,
      message.newEntity.entityAndItsValueAsJSON
    )
  }

  def handleInsert(command: InsertNewEntityCommand): Unit = {
    insertNewEntity(command.newEntity)
  }

  def updateEntity(
    refToLatestVersion: UntypedRef,
    newValue:           EntityAndItsValueAsJSON
  ): Unit = {

    val currentState = getState

    val newState: Option[StateMapSnapshot] =
      currentState.unsafeInsertUpdatedEntity(refToLatestVersion,
                                             newValue)
    setNewState(newState.get)

  }

  val areWeTesting = Config.getDefaultConfig.areWeTesting

  private var applicationState: StateMapSnapshot =
    if (areWeTesting) TestDataProvider.getTestState
    else
      new StateMapSnapshot()

  def getState = {
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

  def insertNewEntity(se: UntypedEntityWithRef): Unit = {
    val oldState = getState
    val newState = oldState.insertVirginEntity(se)
    setNewState(newState)
  }

  // todo-later - this is where the OCC should come

}
