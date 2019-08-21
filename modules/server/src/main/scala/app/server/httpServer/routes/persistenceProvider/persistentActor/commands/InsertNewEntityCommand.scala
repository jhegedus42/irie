package app.server.httpServer.routes.persistenceProvider.persistentActor.commands

import app.shared.state.ApplicationStateMapEntry

case class InsertNewEntityCommand( newEntry: ApplicationStateMapEntry )
