package shared.dataStorage.model

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.relationalWrappers.Ref

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

import CanProvideDefaultValue.defValOf

@JsonCodec
case class TypeName(s: String)

@JsonCodec
case class Note(
  title:   String,
  content: String,
  img:     ImageWithQue)
    extends Value[Note]

object Note {

  import ImageWithQue.defVal

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[Note] {

      override def getDefaultValue: Note = {
        Note(
          "default note title",
          "default note content",
          defValOf[ImageWithQue](defVal)
        )
      }
    }
}

case class ImageWithQue(
  title:               String,
  fileName:            ImgFileName,
  queForPreviousImage: QueForPreviousImage,
  queFromNextImage:    LocationOfQueFromNextImage)
    extends Value[ImageWithQue]

object ImageWithQue {

  implicit val defVal = new CanProvideDefaultValue[ImageWithQue] {

    override def getDefaultValue: ImageWithQue = {
      new ImageWithQue(
        "default image title",
        ImgFileName("defaultImage.jpeg"),
        defValOf[QueForPreviousImage](
          QueForPreviousImage.defaultValue
        ),
        defValOf[LocationOfQueFromNextImage](
          LocationOfQueFromNextImage.canProvideDefaultValue
        )
      )
    }

  }
}

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

object LocationOfQueFromNextImage {

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[LocationOfQueFromNextImage] {

      override def getDefaultValue: LocationOfQueFromNextImage = {
        val r = defValOf[Rect]
        LocationOfQueFromNextImage(r)
      }
    }

}

@JsonCodec
case class QueForPreviousImage(rect: Rect)

object QueForPreviousImage {

  implicit val defaultValue =
    new CanProvideDefaultValue[QueForPreviousImage] {

      override def getDefaultValue: QueForPreviousImage = {
        QueForPreviousImage(defValOf[Rect])
      }
    }
}

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

object Rect {

  implicit val defVal: CanProvideDefaultValue[Rect] =
    new CanProvideDefaultValue[Rect] {

      override def getDefaultValue: Rect = {
        val c    = Coord(50, 50)
        val size = Size(50, 50)
        new Rect(c, size)
      }
    }
}

@JsonCodec
case class ImgFileName(fileNameAsString:String)

