package shared.dataStorage.model

import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.relationalWrappers.Ref
import io.circe.generic.JsonCodec
import CanProvideDefaultValue.defValOf

@JsonCodec
case class Note(
  title:      String,
  content:    String,
  visualHint: HintForNote)
    extends Value[Note]

object Note {

  import HintForNote.defVal

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[Note] {

      override def getDefaultValue: Note = {
        Note(
          "default note title",
          "default note content",
          defValOf[HintForNote](defVal)
        )
      }
    }
}