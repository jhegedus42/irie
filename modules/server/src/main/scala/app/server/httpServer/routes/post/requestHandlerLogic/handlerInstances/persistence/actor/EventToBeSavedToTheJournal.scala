package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.actor

trait EventToBeSavedToTheJournal

case class UpdateEntityEventToBeSavedToTheJournal(
    updateEntityCommand: UpdateEntity_Command)
    extends EventToBeSavedToTheJournal

case class CreateEntityEventToBeSavedToTheJournal(
    insertNewEntityCommand: InsertNewEntity_Command)
