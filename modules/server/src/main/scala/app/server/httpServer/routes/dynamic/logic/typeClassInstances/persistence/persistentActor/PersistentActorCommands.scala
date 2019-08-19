package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor

import app.shared.state.{
  ApplicationStateMapEntry,
  ApplicationStateMap
}

private[persistentActor] object PersistentActorCommands {

  case class UpdateEntityCommand( updatedEntry: ApplicationStateMapEntry )

  case class InsertNewEntityCommand( newEntry: ApplicationStateMapEntry )

  case object GetAllStateCommand

  case object ShutdownActor
}


