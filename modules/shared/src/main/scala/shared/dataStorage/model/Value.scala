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
  img:     VisualHint)
    extends Value[Note]

object Note {

  import VisualHint.defVal

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[Note] {

      override def getDefaultValue: Note = {
        Note(
          "default note title",
          "default note content",
          defValOf[VisualHint](defVal)
        )
      }
    }
}

@JsonCodec
case class VisualHint(
  title:           String,
  fileName:        ImgFileName,
  hintToThisImage: HintToThisImage,
  // this cropped part will be displayed in the
  // previous image as a hint to this image
  placeForHintToNextImage: PlaceForHintToNextImage
  // this is where a hint to the next image
  // will be placed
)

object VisualHint {

  implicit val defVal = new CanProvideDefaultValue[VisualHint] {

    override def getDefaultValue: VisualHint = {
      new VisualHint(
        "default image title",
        defValOf[ImgFileName],
        defValOf[HintToThisImage](
          HintToThisImage.defaultValue
        ),
        defValOf[PlaceForHintToNextImage](
          PlaceForHintToNextImage.canProvideDefaultValue
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
case class PlaceForHintToNextImage(rect: Rect)

object PlaceForHintToNextImage {

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[PlaceForHintToNextImage] {

      override def getDefaultValue: PlaceForHintToNextImage = {
        val r = defValOf[Rect]
        PlaceForHintToNextImage(r)
      }
    }

}

@JsonCodec
case class HintToThisImage(rect: Rect)

object HintToThisImage {

  implicit val defaultValue =
    new CanProvideDefaultValue[HintToThisImage] {

      override def getDefaultValue: HintToThisImage = {
        HintToThisImage(defValOf[Rect])
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
case class ImgFileName(fileNameAsString: String)

object ImgFileName {
  implicit val defVal: CanProvideDefaultValue[ImgFileName] =
    new CanProvideDefaultValue[ImgFileName] {

      override def getDefaultValue: ImgFileName = {
        new ImgFileName("defaultImage.jpeg")
      }
    }

}

