package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.journaledEvents

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.state.ApplicationStateMapEntry

case class DummyCommand() //todo-now
{
  def newEntry:     ApplicationStateMapEntry = ???
  def updatedEntry: ApplicationStateMapEntry = ???
}
