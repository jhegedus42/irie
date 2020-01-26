package shared.testingData

import shared.dataStorage.model.{
  CanProvideDefaultValue,
  LocationInPercentage,
  HeadOfVisualLink,
  ImgFileName,
  TailOfVisualLink,
  Rect,
  SizeInPercentage,
  User,
  VisualHint
}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

object TestDataForImages {

  lazy val aliceEnt: TypedReferencedValue[User] =
    TestEntitiesForUsers.aliceEntity_with_UUID0

  lazy val ar = aliceEnt.ref

  lazy val defaultRect = Rect(LocationInPercentage(50, 50), SizeInPercentage(50, 50))

  def createNewImageWithQue(title: String): VisualHint = {
    CanProvideDefaultValue.defValOf[VisualHint]
  }

//  lazy val backgroundExample = ImgFileName(
//    "6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"
//  )
//
//  lazy val hintExample = ImgFileName(
//    "befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"
//  )
//
//  lazy val backround = VisualHint(
//    "supernova",
//    backgroundExample,
//    ???,
//    ???
////    HintToThisImage(Coord(100,200), Crop(),),
////    ???
//  )

}
