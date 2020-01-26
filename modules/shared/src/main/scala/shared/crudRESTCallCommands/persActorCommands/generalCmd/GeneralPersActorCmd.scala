package shared.crudRESTCallCommands.persActorCommands.generalCmd

import io.circe.{Decoder, Json, ParsingFailure}
import io.circe.Decoder.Result
import io.circe.generic.JsonCodec
import io.circe.parser.parse
import shared.crudRESTCallCommands.{CanProvideRouteName, JSONConvertable}
import shared.crudRESTCallCommands.persActorCommands.PersActorCommand
import shared.crudRESTCallCommands.persActorCommands.crudCMDs.InsertEntityPersActCmd

@JsonCodec
case class GeneralPersActorCmd(commandAsString:String)
  extends PersActorCommand

object GeneralPersActorCmd {

  object CommandStrings{
    val saveData="save"
  }

  implicit val users: CanProvideRouteName[GeneralPersActorCmd] =
    new CanProvideRouteName[GeneralPersActorCmd] {

      override def getRouteName: String =
        "GeneralPersActorCmd"
    }

  implicit val jSONConvertable
  : JSONConvertable[GeneralPersActorCmd] =
    new JSONConvertable[GeneralPersActorCmd] {
      import io.circe._
      import io.circe.generic.JsonCodec
      import io.circe.generic.auto._
      import io.circe.parser._
      import io.circe.syntax._

      override def toJSON(v: GeneralPersActorCmd): String =
        v.asJson.spaces4

      override def fromJSONToObject(
                                     json: String
                                   ): GeneralPersActorCmd = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)

        val res1: Json = jsonParsed.toOption.get

        val decoder =
          implicitly[Decoder[GeneralPersActorCmd]]

        val res2: Result[GeneralPersActorCmd] =
          decoder.decodeJson(res1)

        res2.toOption.get
      }

    }

}

