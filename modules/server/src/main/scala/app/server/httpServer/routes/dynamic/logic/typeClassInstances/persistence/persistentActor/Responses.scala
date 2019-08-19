package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor

import app.shared.state.{ApplicationStateMap, StateChange}

private[persistence] object Responses {
  case class GetStateResult( state: ApplicationStateMap )
  case class InsertNewEntityCommandResponse(stateChange :
                                            StateChange)
}
