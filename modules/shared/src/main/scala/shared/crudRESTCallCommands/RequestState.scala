package shared.crudRESTCallCommands

import io.circe.generic.JsonCodec

sealed trait RequestState

@JsonCodec
case class RequestIsOnItsWayTowardsServer() extends RequestState

@JsonCodec
case class RequestSuccessfullyProcessedInPersistentActor()
    extends RequestState

@JsonCodec
case class RequestReturnedWithError(errorDescription: String)
    extends RequestState
