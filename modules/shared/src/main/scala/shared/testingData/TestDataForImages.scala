package shared.testingData

import shared.dataStorage.{
  Coord,
  ImageWithQue,
  LocationOfQueFromNextImage,
  QueForPreviousImage,
  Rect,
  Size,
  TypedReferencedValue,
  User
}
import shared.testingData.TestDataStore.aliceUserEnt

object TestDataForImages {

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref

  lazy val defaultRect = Rect(Coord(50, 50), Size(50, 50))

  def createNewImageWithQue(title: String): ImageWithQue = {
    val t = ImageWithQue(
      title,
      None,
      None,
      Some(QueForPreviousImage(defaultRect)),
      Some(LocationOfQueFromNextImage(defaultRect))
    )
    t
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
