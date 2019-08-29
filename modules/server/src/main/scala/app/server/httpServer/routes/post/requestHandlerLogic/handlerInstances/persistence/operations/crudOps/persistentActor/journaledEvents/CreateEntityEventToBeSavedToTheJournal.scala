package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.journaledEvents

case class CreateEntityEventToBeSavedToTheJournal(
  insertNewEntityCommand: DummyCommand)
    extends EventToBeSavedToTheJournal
