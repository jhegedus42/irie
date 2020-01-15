package shared.dataStorage

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

/**
  *
  * This is the type of the value that an entity holds.
  *
  * @tparam T
  */

sealed trait Value[+T <: Value[T]]

//@JsonCodec
//object Value {
//
//  def getName[T <: Value[T]](implicit classTag: ClassTag[T]):TypeName =
//    TypeName(classTag.getClass.getSimpleName)
//}

@JsonCodec
case class TypeName(s: String)

@JsonCodec
case class Note(
                 title:   String,
                 content: String,
                 folderR:Option[Ref[NoteFolder]])
    extends Value[Note]

@JsonCodec
case class Image(
  title:           String,
  referenceToNote: Option[Ref[Note]],
  fileName:        Option[String])
    extends Value[Image]

@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password:       String = "titok")
    extends Value[User]

@JsonCodec
case class NoteFolder(
  name: String)
    extends Value[NoteFolder]



