package app.server.httpServer.routes.persistenceProvider.persistentActor.events

import app.server.httpServer.routes.persistenceProvider.persistentActor.commands.{InsertNewEntity_Command, UpdateEntity_Command}

sealed trait Event

case class CreateEntityEvent(insertNewEntityCommand: InsertNewEntity_Command )
    extends Event

case class UpdateEntityEvent(updateEntityCommand: UpdateEntity_Command )
  extends Event
