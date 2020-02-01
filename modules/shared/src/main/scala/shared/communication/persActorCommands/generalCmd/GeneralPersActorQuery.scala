package shared.communication.persActorCommands.generalCmd

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Json, ParsingFailure}
import io.circe.Decoder.Result
import io.circe.parser.parse
import shared.communication.{CanProvideRouteName, JSONConvertable}
import shared.communication.persActorCommands.PersActorQuery
import shared.communication.persActorCommands.crudCMDs.InsertEntityPersActCmd

@JsonCodec
case class GeneralPersActorQuery(commandAsString:String)
  extends PersActorQuery

object GeneralPersActorQuery {


  object CommandStrings{
    val saveData="save"
  }

  implicit val users: CanProvideRouteName[GeneralPersActorQuery] =
    new CanProvideRouteName[GeneralPersActorQuery] {

      override def getRouteName: String =
        "GeneralPersActorCmd"
    }

  implicit val jSONConvertable
  : JSONConvertable[GeneralPersActorQuery] =
    new JSONConvertable[GeneralPersActorQuery] {
      import io.circe._
      import io.circe.generic.JsonCodec
      import io.circe.generic.auto._
      import io.circe.parser._
      import io.circe.syntax._

      override def toJSON(v: GeneralPersActorQuery): String =
        v.asJson.spaces4

      override def fromJSONToObject(
                                     json: String
                                   ): GeneralPersActorQuery = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)

        val res1: Json = jsonParsed.toOption.get

        val decoder =
          implicitly[Decoder[GeneralPersActorQuery]]

        val res2: Result[GeneralPersActorQuery] =
          decoder.decodeJson(res1)

        res2.toOption.get
      }

    }

}

