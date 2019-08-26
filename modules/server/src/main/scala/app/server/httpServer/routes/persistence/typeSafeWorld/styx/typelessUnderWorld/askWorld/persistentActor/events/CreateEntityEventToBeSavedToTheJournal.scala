package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor.events

import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor.commands.InsertNewEntity_Command

case class CreateEntityEventToBeSavedToTheJournal(insertNewEntityCommand: InsertNewEntity_Command )
    extends EventToBeSavedToTheJournal
