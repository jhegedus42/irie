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
trait Value[+T <: Value[T]]

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
case class HintForNote(
  hint:        ImgHintToThisNotesText,
  rectForHead: RectForHead,
  // this cropped part will be displayed in the
  // previous image as a hint to this image
  rectForTail: RectForTail
  // this is where a hint to the next image
  // will be placed
)

object HintForNote {

  implicit val defVal =
    new CanProvideDefaultValue[HintForNote] {

      override def getDefaultValue: HintForNote = {
        new HintForNote(
          defValOf[ImgHintToThisNotesText],
          defValOf[RectForHead](
            RectForHead.defaultValue
          ),
          defValOf[RectForTail](
            RectForTail.canProvideDefaultValue
          )
        )
      }

    }
}



@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password: String = "defaultPWD")
    extends Value[User]


@JsonCodec
case class Folder(
  name:  String,
  notes: List[Ref[Note]])
    extends Value[Folder]

@JsonCodec
case class RectForTail(rect: Rect)

object RectForTail {

  implicit val canProvideDefaultValue =
    new CanProvideDefaultValue[RectForTail] {

      override def getDefaultValue: RectForTail = {
        val r = defValOf[Rect]
        RectForTail(r)
      }
    }

}

@JsonCodec
case class RectForHead(rect: Rect)

object RectForHead {

  implicit val defaultValue =
    new CanProvideDefaultValue[
      RectForHead
    ] {

      override def getDefaultValue: RectForHead = {
        RectForHead(defValOf[Rect])
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
case class ImgFileName(fileNameAsString: String) {
  def fileNameWithPathAsString = s"./images/${fileNameAsString}"
}

object ImgFileName {

  implicit val defVal: CanProvideDefaultValue[ImgFileName] =
    new CanProvideDefaultValue[ImgFileName] {

      override def getDefaultValue: ImgFileName = {
        new ImgFileName("defaultImage.jpeg")
      }
    }

}
