package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.StateMapEntry

trait EventToBeSavedIntoJournal



case class InsertEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal
case class UpdateEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal

case class DummyEventPayload()
{
  def newEntry:     StateMapEntry = ??? // todo-next
  def updatedEntry: StateMapEntry = ???
}
