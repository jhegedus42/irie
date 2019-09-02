package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.{StateMapEntry, StateMapSnapshot}
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import app.shared.utils.UUID_Utils.EntityIdentity

object Commands {
  sealed trait Command
  case object GetStateSnapshot extends Command
  case class InsertCommand(newEntry:     StateMapEntry) extends Command
  case class Update(updatedEntry: StateMapEntry) extends Command
  case object ShutdownActor extends Command
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

object Payloads {
  case class UntypedRefWithoutVersion(
      entityValueTypeAsString: EntityValueTypeAsString,
      entityIdentity:          EntityIdentity = EntityIdentity()
  )

  object UntypedRefWithoutVersion {
    implicit def makeFromRefToEntity[T <: EntityValue[T]](
        refToEntity: RefToEntityWithoutVersion[T]
    ): UntypedRefWithoutVersion = {
      UntypedRefWithoutVersion(
        entityValueTypeAsString = refToEntity.entityValueTypeAsString,
        entityIdentity          = refToEntity.entityIdentity
      )
    }

  }

}
