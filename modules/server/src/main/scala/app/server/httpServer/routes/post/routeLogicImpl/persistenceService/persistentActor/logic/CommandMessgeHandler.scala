package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.{
  InsertNewEntityCommand,
  UpdateEntityCommand
}

case class CommandMessgeHandler(val stateService: StateService) {

  def handleUpdate(message: UpdateEntityCommand): DidOperationSucceed = {
    stateService.updateEntity(message.updatedCurrentEntity.untypedRef,
                              message.newEntity.entityValueAsJSON)
  }

  def handleInsert(command: InsertNewEntityCommand): DidOperationSucceed = {
    stateService.insertNewEntity(command.newEntity)
  }

}
