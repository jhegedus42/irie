package app.server.httpServer.routes.persistentActor

import state.UntypedEntityWithRef

trait EventToBeSavedIntoJournal



case class InsertEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal
case class UpdateEvent(data: DummyEventPayload)
  extends EventToBeSavedIntoJournal

case class DummyEventPayload()
{
  def newEntry:     UntypedEntityWithRef = ???
  // todo-next => ??? what is this here ^^^ ???
  //  maybe this is needed to make
  //  actual persistence work ?
  def updatedEntry: UntypedEntityWithRef = ???
}
