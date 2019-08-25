package app.server.httpServer.routes.persistenceProvider.persistentActor.commands

import app.server.httpServer.routes.persistenceProvider.persistentActor.state.ApplicationStateMapEntry


case class InsertNewEntity_Command(newEntry: ApplicationStateMapEntry )
