package shared.communication.persActorCommands.auth

import io.circe.{Decoder, Encoder}
import shared.communication.CanProvideRouteName
import shared.communication.persActorCommands.{Query, Response}
import shared.dataStorage.model.PWDNotHashed

object EncoderForAjax {

  def encoder[Q <: Query: CanProvideRouteName: Encoder: Decoder](
    in:  Q,
    pwd: PWDNotHashed
  )(
    implicit
    encoder:     Encoder[QueryAuthWrapper[Q]],
    respDecoder: Decoder[Response[Q]]
  ): String = {

    ???
  }

}
