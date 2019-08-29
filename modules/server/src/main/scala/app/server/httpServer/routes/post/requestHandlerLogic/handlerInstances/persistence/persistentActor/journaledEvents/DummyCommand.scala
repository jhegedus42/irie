package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.journaledEvents

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.ApplicationStateMapEntry

case class DummyCommand() //todo-now
{
  def newEntry:     ApplicationStateMapEntry = ???
  def updatedEntry: ApplicationStateMapEntry = ???
}
