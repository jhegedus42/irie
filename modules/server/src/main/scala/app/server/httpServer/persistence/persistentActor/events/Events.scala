package app.server.httpServer.persistence.persistentActor.events

import app.server.httpServer.persistence.persistentActor.PersistentActorCommands.InsertNewEntityCommand


//events
  sealed trait Event
  //    case class UpdateEntityEvent[E <: Entity[E]](entity: UntypedRef )
  //        extends Event

  case class CreateEntityEvent(insertNewEntity: InsertNewEntityCommand) extends Event

