package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.journaledEvents

case class CreateEntityEventToBeSavedToTheJournal(
  insertNewEntityCommand: DummyCommand)
    extends EventToBeSavedToTheJournal
