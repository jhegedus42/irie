package app.server.httpServer.routes.persistenceProvider.persistentActor

import app.server.httpServer.routes.persistenceProvider.persistentActor.Errors.EntityUpdateVersionError
import app.server.httpServer.routes.persistenceProvider.persistentActor.state.{ApplicationStateMap, StateChange}

private[persistenceProvider] object Responses {

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(stateChange: StateChange )

  case class UpdateEntity_Command_Response(
      result: Either[EntityUpdateVersionError, StateChange]
  )

}
