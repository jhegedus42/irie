package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.journaledEvents

case class UpdateEntityEventToBeSavedToTheJournal(
//    updateEntityCommand: UpdateEntity_Command)
  updateEntityCommand: DummyCommand) // todo-now
    extends EventToBeSavedToTheJournal
