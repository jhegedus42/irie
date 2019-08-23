package app.server.httpServer.routes.persistenceProvider.persistentActor.commands

import app.server.httpServer.routes.persistenceProvider.persistentActor.state.ApplicationStateMapEntry

case class UpdateEntityCommand( updatedEntry: ApplicationStateMapEntry )
