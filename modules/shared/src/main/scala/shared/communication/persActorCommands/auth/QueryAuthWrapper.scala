package shared.communication.persActorCommands.auth

import io.circe.{Decoder, Encoder}
import shared.dataStorage.model.PWDNotHashed
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.relationalWrappers.Ref
import shared.communication.persActorCommands.PersActorQuery

@JsonCodec
case class QueryAuthWrapper(
  query: String,
  pwd:   PWDNotHashed)

// todo-now JSON encoding for subtypes

// option like cucc
