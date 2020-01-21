package shared.testingData

import shared.dataStorage.model.{
  CanProvideDefaultValue,
  Coord,
  VisualHint,
  PlaceForHintToNextImage,
  HintToThisImage,
  Rect,
  Size,
  User
}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

object TestDataForImages {

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref

  lazy val defaultRect = Rect(Coord(50, 50), Size(50, 50))

  def createNewImageWithQue(title: String): VisualHint = {
    CanProvideDefaultValue.defValOf[VisualHint]
  }

  val appleImage = TypedReferencedValue(
    createNewImageWithQue("apple")
  ).addEntityOwnerInfo(ar)

  val starImage =
    TypedReferencedValue(createNewImageWithQue("star"))
      .addEntityOwnerInfo(ar)

  val musicImage = TypedReferencedValue(
    createNewImageWithQue("music")
  ).addEntityOwnerInfo(ar)

}
