package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.journaledEvents

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.ApplicationStateMapEntry

trait EventToBeSavedToTheJournal

case class DummyCommand() //todo-now
{
  def newEntry:     ApplicationStateMapEntry = ???
  def updatedEntry: ApplicationStateMapEntry = ???
}

case class UpdateEntityEventToBeSavedToTheJournal(
//    updateEntityCommand: UpdateEntity_Command)
  updateEntityCommand: DummyCommand) // todo-now
    extends EventToBeSavedToTheJournal

case class CreateEntityEventToBeSavedToTheJournal(
  insertNewEntityCommand: DummyCommand)
    extends EventToBeSavedToTheJournal //todo-now
