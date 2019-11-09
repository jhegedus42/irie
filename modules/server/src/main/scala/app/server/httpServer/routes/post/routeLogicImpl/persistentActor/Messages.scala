package app.server.httpServer.routes.post.routeLogicImpl.persistentActor

import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.state.{StateMapSnapshot, UntypedEntityWithRef}

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

object Payloads {
//  case class UntypedRefWithoutVersion(
//      entityValueTypeAsString: EntityValueTypeAsString,
//      entityIdentity:          EntityIdentity = EntityIdentity()
//  )

//  object UntypedRefWithoutVersion {
//    implicit def makeFromRefToEntity[T <: EntityValue[T]](
//        refToEntity: RefToEntityWithVersion[T]
//    ): UntypedRefWithoutVersion = {
//      UntypedRefWithoutVersion(
//        entityValueTypeAsString = refToEntity.entityValueTypeAsString,
//        entityIdentity          = refToEntity.entityIdentity
//      )
//    }
//
//  }

}