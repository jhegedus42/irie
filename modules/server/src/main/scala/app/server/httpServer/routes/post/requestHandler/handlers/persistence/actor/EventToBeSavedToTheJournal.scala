package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

trait EventToBeSavedToTheJournal

case class UpdateEntityEventToBeSavedToTheJournal(
    updateEntityCommand: UpdateEntity_Command)
    extends EventToBeSavedToTheJournal

case class CreateEntityEventToBeSavedToTheJournal(
    insertNewEntityCommand: InsertNewEntity_Command)
