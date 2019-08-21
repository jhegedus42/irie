package app.server.httpServer.routes.persistenceProvider.persistentActor

import app.shared.state.{ApplicationStateMap, StateChange}

private[persistenceProvider] object Responses {
  case class GetStateResult( state: ApplicationStateMap )
  case class InsertNewEntityCommandResponse(stateChange :
                                            StateChange)
}
