package shared.communication.persActorCommands

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json, ParsingFailure}
import io.circe.generic.JsonCodec
import io.circe.parser.parse
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd

//import cats.instances.either._

@JsonCodec
case class SuccessOrFailure(result:Option[String])

@JsonCodec
case class Response[CMD](
  cmd:   CMD,
  error: SuccessOrFailure)

object Response {
}
