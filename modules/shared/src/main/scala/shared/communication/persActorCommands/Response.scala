package shared.communication.persActorCommands

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json, ParsingFailure}
import io.circe.generic.JsonCodec
import io.circe.parser.parse
import shared.communication.JSONConvertable
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd

//import cats.instances.either._

@JsonCodec
case class SuccessOrFailure(result:Option[String])

@JsonCodec
case class Response[CMD](
  cmd:   CMD,
  error: SuccessOrFailure)

object Response {

  implicit def jSONConvertable[
    CMD: Decoder: Encoder
  ]: JSONConvertable[Response[CMD]] =
    new JSONConvertable[Response[CMD]] {
      import io.circe._
      import io.circe.generic.JsonCodec
      import io.circe.generic.auto._
      import io.circe.parser._
      import io.circe.syntax._

      override def toJSON(v: Response[CMD]): String =
        v.asJson.spaces4

      override def fromJSONToObject(json: String): Response[CMD] = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)
        val res1: Json = jsonParsed.toOption.get
        val decoder =
          implicitly[Decoder[Response[CMD]]]
        val res2: Result[Response[CMD]] =
          decoder.decodeJson(res1)
        res2.toOption.get
      }

    }

}