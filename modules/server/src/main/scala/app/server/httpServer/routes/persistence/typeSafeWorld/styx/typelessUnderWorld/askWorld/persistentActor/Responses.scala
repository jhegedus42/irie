package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor

import app.server.httpServer.routes.persistence.typeSafeWorld.errors.AreWeHappy
import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor.Errors.EntityUpdateVersionError
import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state.{ApplicationStateMap, StateChange}

private[styx] object Responses {

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(stateChange: StateChange )

  case class UpdateEntity_Command_Response(areWeHappy: AreWeHappy )

}
