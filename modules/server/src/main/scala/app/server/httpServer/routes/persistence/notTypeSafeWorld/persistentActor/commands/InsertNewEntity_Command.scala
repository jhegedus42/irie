package app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.commands

import app.server.httpServer.routes.persistence.notTypeSafeWorld.ApplicationStateMapEntry


case class InsertNewEntity_Command(newEntry: ApplicationStateMapEntry )
