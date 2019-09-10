package app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.UntypedEntity

trait EventToBeSavedIntoJournal



case class InsertEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal
case class UpdateEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal

case class DummyEventPayload()
{
  def newEntry:     UntypedEntity = ??? // todo-next
  def updatedEntry: UntypedEntity = ???
}
