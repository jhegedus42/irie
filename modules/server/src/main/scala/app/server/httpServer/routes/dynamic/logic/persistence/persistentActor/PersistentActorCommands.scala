package app.server.httpServer.routes.dynamic.logic.persistence.persistentActor

import app.server.httpServer.routes.dynamic.logic.persistence.persistentActor.state.{
  ApplicationStateMapEntry,
  ApplicationStateMap
}

private[persistentActor] object PersistentActorCommands {

  case class UpdateEntityCommand( updatedEntry: ApplicationStateMapEntry )

  case class InsertNewEntityCommand( newEntry: ApplicationStateMapEntry )

  case object GetAllStateCommand

  case object ShutdownActor
}

private[persistence] object Responses {
  case class GetStateResult( state: ApplicationStateMap )
  case class InsertNewEntityCommandResponse(stateChange :
                                            StateChange)
}
