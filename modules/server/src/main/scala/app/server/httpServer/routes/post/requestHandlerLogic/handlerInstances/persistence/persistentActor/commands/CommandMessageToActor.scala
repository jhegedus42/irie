package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.ApplicationStateMapEntry

sealed trait CommandMessageToActor

case object GetApplicationState

case class Insert(newEntry: ApplicationStateMapEntry )
    extends CommandMessageToActor
case class Update(updatedEntry: ApplicationStateMapEntry )
case object Shutdown
