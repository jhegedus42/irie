package app.server.persistence.persistentActor

import app.server.persistence.state.{ApplicationState, ApplicationStateEntry}

object Commands {


  case class UpdateEntity( updatedEntry:ApplicationStateEntry )

  case class CreateEntity( newEntry:ApplicationStateEntry )

  case object GetAllState

  case class GetStateResult( state: ApplicationState )

  case object ShutdownActor
}
