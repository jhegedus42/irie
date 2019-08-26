package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor.events

import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor.commands.UpdateEntity_Command

case class UpdateEntityEventToBeSavedToTheJournal(
    updateEntityCommand: UpdateEntity_Command
) extends EventToBeSavedToTheJournal
