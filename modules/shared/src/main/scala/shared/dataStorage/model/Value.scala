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
  fileData:        ImgFileData,
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
        defValOf[ImgFileData],
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
case class ImgFileData(
  fileName:    ImgFileName,
  sizeInPixel: SizeInPixel)

object ImgFileData {

  implicit val defVal: CanProvideDefaultValue[ImgFileData] =
    new CanProvideDefaultValue[ImgFileData] {

      override def getDefaultValue: ImgFileData = {
        val c    = defValOf[ImgFileName]
        val size = SizeInPixel(20, 20)
        new ImgFileData(c, size)

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
