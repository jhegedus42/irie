package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.ApplicationStateMapEntry

sealed trait CommandMessageToActor

case object GetApplicationState

case class  Insert(newEntry: ApplicationStateMapEntry ) extends CommandMessageToActor
case class  Update(updatedEntry: ApplicationStateMapEntry )
case object Shutdown
