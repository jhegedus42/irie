package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.StateMapEntry

trait JournalEntries



case class InsertEventEntries(data: DummyEventPayload)
  extends JournalEntries
case class UpdateEventEntries(data: DummyEventPayload) // todo-later
  extends JournalEntries

case class DummyEventPayload() //todo-now
{
  def newEntry:     StateMapEntry = ???
  def updatedEntry: StateMapEntry = ???
}
