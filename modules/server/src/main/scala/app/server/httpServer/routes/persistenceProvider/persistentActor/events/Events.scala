package app.server.httpServer.routes.persistenceProvider.persistentActor.events

import app.server.httpServer.routes.persistenceProvider.persistentActor.PersistentActorCommands.InsertNewEntityCommand


//events
  sealed trait Event
  //    case class UpdateEntityEvent[E <: Entity[E]](entity: UntypedRef )
  //        extends Event

  case class CreateEntityEvent(insertNewEntity: InsertNewEntityCommand) extends Event

