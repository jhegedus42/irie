package app.server.httpServer.routes.persistentActor

import refs.RefToEntityWithVersion
import state.{StateMapSnapshot, UntypedEntityWithRef}

object Commands {
  sealed trait Command
  case object GetStateSnapshot extends Command
  case class InsertNewEntityCommand(newEntity: UntypedEntityWithRef)
      extends Command

  case class UpdateEntityCommand(
                                  updatedCurrentEntity: UntypedEntityWithRef,
                                  newEntity:            UntypedEntityWithRef
  ) extends Command

  case object ShutdownActor extends Command
  case object ResetStateCommand extends Command
}

case class DummyResponsePayload()

object Responses {
  sealed trait PersistentActorResponse
  case class GetStateResponse(state: StateMapSnapshot)
      extends PersistentActorResponse
  case class InsertActResp(dummy: DummyResponsePayload)
      extends PersistentActorResponse
  case class UpdateActResp(areWeHappy: DummyResponsePayload)
      extends PersistentActorResponse
}


