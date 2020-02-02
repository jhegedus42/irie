package shared.communication.persActorCommands.generalCmd

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Json, ParsingFailure}
import io.circe.Decoder.Result
import io.circe.parser.parse
import shared.communication.{CanProvideRouteName }
import shared.communication.persActorCommands.Query
import shared.communication.persActorCommands.crudCMDs.InsertEntityPersActCmd

@JsonCodec
case class AdminQuery(commandAsString:String)
  extends Query

object AdminQuery {


  object CommandStrings{
    val saveData="save"
  }

  implicit val users: CanProvideRouteName[AdminQuery] =
    new CanProvideRouteName[AdminQuery] {

      override def getRouteName: String =
        "GeneralPersActorCmd"
    }

}

