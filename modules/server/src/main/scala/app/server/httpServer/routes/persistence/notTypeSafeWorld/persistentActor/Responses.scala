package app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor

import app.server.httpServer.routes.persistence.notTypeSafeWorld.StateChange
import app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.Errors.EntityUpdateVersionError
import app.server.httpServer.routes.persistence.notTypeSafeWorld.state.ApplicationStateMap

private[persistence] object Responses {

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(stateChange: StateChange )

  case class UpdateEntity_Command_Response(
      result: Either[EntityUpdateVersionError, StateChange]
  )

}
