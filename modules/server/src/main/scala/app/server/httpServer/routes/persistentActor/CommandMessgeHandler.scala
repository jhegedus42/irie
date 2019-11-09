package app.server.httpServer.routes.persistentActor

import app.server.httpServer.routes.persistentActor.Commands.{InsertNewEntityCommand, UpdateEntityCommand}

case class CommandMessgeHandler(val stateService: StateService) {

  def handleUpdate(
    message: UpdateEntityCommand
  ) : Unit = {
    stateService.updateEntity(
      message.updatedCurrentEntity.untypedRef,
      message.newEntity.entityAndItsValueAsJSON
    )
  }

  def handleInsert(
    command: InsertNewEntityCommand
   ) : Unit = {
    stateService.insertNewEntity(command.newEntity)
  }

}
