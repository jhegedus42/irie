package app.shared.data.model

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.TypedRef

object SimpleModel {

  case class Note(title: String, text: String ) extends Entity

  case class ListOfNotes(name: String, lines: List[TypedRef[Note]] = List() ) extends Entity

}
