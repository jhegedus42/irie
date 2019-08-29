package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.StateMapEntry

trait EventToBeSavedToTheJournal



case class InsertEvent(data: DummyEventPayload)
  extends EventToBeSavedToTheJournal
case class UpdateEvent(data: DummyEventPayload) // todo-later
  extends EventToBeSavedToTheJournal

case class DummyEventPayload() //todo-now
{
  def newEntry:     StateMapEntry = ???
  def updatedEntry: StateMapEntry = ???
}
