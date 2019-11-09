package app.server.httpServer.routes.post.routeLogicImpl.persistentActor

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.Commands.{InsertNewEntityCommand, UpdateEntityCommand}

case class CommandMessgeHandler(val stateService: StateService) {

  def handleUpdate(
    message: UpdateEntityCommand
  ): DidOperationSucceed = {
    stateService.updateEntity(
      message.updatedCurrentEntity.untypedRef,
      message.newEntity.entityAndItsValueAsJSON
    )
  }

  def handleInsert(
    command: InsertNewEntityCommand
  ): DidOperationSucceed = {
    stateService.insertNewEntity(command.newEntity)
  }

}
