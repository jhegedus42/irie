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
  content: String)
    extends Value[Note]

@JsonCodec
case class ImageWithQue(
  title:               String,
  referenceToNote:     Option[Ref[Note]],
  fileName:            Option[String],
  queForPreviousImage: Option[QueForPreviousImage],
  queFromNextImage:    Option[LocationOfQueFromNextImage])
    extends Value[ImageWithQue]

@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password:       String = "titok")
    extends Value[User]

@JsonCodec
case class Folder(
  name:  String,
  notes: List[Ref[Note]])
    extends Value[Folder]

@JsonCodec
case class LocationOfQueFromNextImage(rect: Rect)

@JsonCodec
case class QueForPreviousImage(rect: Rect)

@JsonCodec
case class Coord(
  x: Int,
  y: Int)

@JsonCodec
case class Size(
  width:  Int,
  height: Int)

@JsonCodec
case class Rect(
  center: Coord,
  size:   Size)
