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
  title:      String,
  content:    String,
  visualHint: VisualHint)
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
  imgHintToThisNotesText: ImgHintToThisNotesText,
  hintToNextNotesImage:   HeadOfVisualLink,
  // this cropped part will be displayed in the
  // previous image as a hint to this image
  tailOfVisualLinkFromThisNoteToNextNote: TailOfVisualLink
  // this is where a hint to the next image
  // will be placed
)

object VisualHint {

  implicit val defVal =
    new CanProvideDefaultValue[VisualHint] {

      override def getDefaultValue: VisualHint = {
        new VisualHint(
          defValOf[ImgHintToThisNotesText],
          defValOf[HeadOfVisualLink](
            HeadOfVisualLink.defaultValue
          ),
          defValOf[TailOfVisualLink](
            TailOfVisualLink.canProvideDefaultValue
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
case class TailOfVisualLink(rect: Rect)

object TailOfVisualLink {

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[TailOfVisualLink] {

      override def getDefaultValue: TailOfVisualLink = {
        val r = defValOf[Rect]
        TailOfVisualLink(r)
      }
    }

}

@JsonCodec
case class HeadOfVisualLink(rect: Rect)

object HeadOfVisualLink {

  implicit val defaultValue =
    new CanProvideDefaultValue[
      HeadOfVisualLink
    ] {

      override def getDefaultValue: HeadOfVisualLink = {
        HeadOfVisualLink(defValOf[Rect])
      }
    }
}

@JsonCodec
case class LocationInPercentage(
  xInPercentage: Double,
  yInPercentage: Double) {

  def toLocationInPixel(sizeInPixel: SizeInPixel): LocationInPixel = {
    LocationInPixel(
      sizeInPixel.width * (xInPercentage / 100.0),
      sizeInPixel.height * (yInPercentage / 100.0)
    )
  }
}

@JsonCodec
case class LocationInPixel(
  xInPixel: Double,
  yInPixel: Double)

@JsonCodec
case class ImgHintToThisNotesText(
  fileName:    ImgFileName,
  sizeInPixel: SizeInPixel)

object ImgHintToThisNotesText {

  implicit val defVal
    : CanProvideDefaultValue[ImgHintToThisNotesText] =
    new CanProvideDefaultValue[ImgHintToThisNotesText] {

      override def getDefaultValue: ImgHintToThisNotesText = {
        val c    = defValOf[ImgFileName]
        val size = SizeInPixel(20, 20)
        new ImgHintToThisNotesText(c, size)

      }
    }

}

@JsonCodec
case class SizeInPercentage(
  width:  Double,
  height: Double) {

  def toSizeInPixel(sizeInPixel: SizeInPixel): SizeInPixel = {
    SizeInPixel(
      sizeInPixel.width * (width / 100.0),
      sizeInPixel.height * (height / 100.0)
    )
  }
}

@JsonCodec
case class SizeInPixel(
  width:  Double,
  height: Double)

object SizeInPixel {}

@JsonCodec
case class Rect(
  upperLeftCornerXYInPercentage: LocationInPercentage,
  sizeInPercentage:              SizeInPercentage)

object Rect {

  implicit val defVal: CanProvideDefaultValue[Rect] =
    new CanProvideDefaultValue[Rect] {

      override def getDefaultValue: Rect = {
        val coordInPercentage = LocationInPercentage(50, 50)
        val sizeInPercentage  = SizeInPercentage(20, 20)
        new Rect(coordInPercentage, sizeInPercentage)
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
