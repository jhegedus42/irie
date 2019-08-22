package app.server.httpServer.routes.persistenceProvider.persistentActor.events

import app.server.httpServer.routes.persistenceProvider.persistentActor.commands.{InsertNewEntityCommand, UpdateEntityCommand}

sealed trait Event

case class CreateEntityEvent(insertNewEntityCommand: InsertNewEntityCommand )
    extends Event

case class UpdateEntityEvent(updateEntityCommand: UpdateEntityCommand )
  extends Event
