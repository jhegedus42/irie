package app.server.httpServer.persistence.persistentActor

import app.server.httpServer.persistence.state.{ApplicationStateMap, ApplicationStateEntry}

private[persistentActor] object PersistentActorCommands {


  case class UpdateEntity( updatedEntry:ApplicationStateEntry )

  case class InsertNewEntity(newEntry:ApplicationStateEntry )

  case object GetAllState

  case class GetStateResult( state: ApplicationStateMap )

  case object ShutdownActor
}
