package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.events

import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.PersistentActorCommands.InsertNewEntityCommand


//events
  sealed trait Event
  //    case class UpdateEntityEvent[E <: Entity[E]](entity: UntypedRef )
  //        extends Event

  case class CreateEntityEvent(insertNewEntity: InsertNewEntityCommand) extends Event

