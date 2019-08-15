package app.server.httpServer.persistence.persistentActor

import app.server.httpServer.persistence.state.{ApplicationState, ApplicationStateEntry}

private[persistentActor] object Commands {


  case class UpdateEntity( updatedEntry:ApplicationStateEntry )

  case class CreateEntity( newEntry:ApplicationStateEntry )

  case object GetAllState

  case class GetStateResult( state: ApplicationState )

  case object ShutdownActor
}
