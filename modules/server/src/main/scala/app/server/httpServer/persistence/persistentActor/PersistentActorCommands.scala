package app.server.httpServer.persistence.persistentActor

import app.server.httpServer.persistence.persistentActor.state.{
  ApplicationStateEntry,
  ApplicationState
}

object PersistentActorCommands {

  case class UpdateEntityCommand( updatedEntry: ApplicationStateEntry )

  case class InsertNewEntityCommand( newEntry: ApplicationStateEntry )

  case object GetAllStateCommand

  case object ShutdownActor
}

private[persistentActor] object Responses {
  case class GetStateResult( state: ApplicationState )
  case class InsertNewEntityCommandResponse(stateChange : StateChange)
}
