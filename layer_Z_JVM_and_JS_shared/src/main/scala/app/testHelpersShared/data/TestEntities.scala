package app.testHelpersShared.data

import app.shared.data.model.{TypeAsString, LineText, LineWithQue, User, UserLineList}
import app.shared.data.ref.{Ref, RefDyn, RefVal, RefValDyn, Version}

/**
  * Created by joco on 09/10/2017.
  */
object TestEntities {
  val theUUIDofTheLine =   "4ce6fca0-0fd5-4197-a946-90f5e7e00d9d"
  val dummyUUID2: String = "4ce6fca0-0fd5-4197-a946-90f5e7e00d9e"
  val line                     = LineText(title= "rubadub",text="text")
  //    private[this] val line                     = Line()
  val refToLine: Ref[LineText] = Ref.makeWithUUID(theUUIDofTheLine)




  val refValOfLineV0           = RefVal(refToLine, line, Version())



  val refValOfLineV1           = RefVal(refToLine, line, Version().inc())

}

object TestEntitiesForStateThree
{
  lazy val userEntity         = User(name = "joco",password="macska")
  lazy val user1uuid            = "10000000-0000-0000-0000-000000000001"
  lazy val rvdUser: RefValDyn = RefValDyn(RefDyn(user1uuid, TypeAsString.fromEntity(userEntity)), userEntity, Version())
  lazy val userRef: Ref[User] = rvdUser.r.toRef[User]().toEither.right.get

  lazy val line1                     = LineText(title = "l1",text="text")
  lazy val line2                     = LineText(title = "l2",text="text")
  lazy val line3                     = LineText(title = "l3",text="text")
  lazy val entityTypeL: TypeAsString = TypeAsString.make[LineText]

  lazy val line1uuid            = "00000000-0000-0000-0000-000000000001"
  lazy val line2uuid            = "00000000-0000-0000-0000-000000000002"
  lazy val line3uuid            = "00000000-0000-0000-0000-000000000003"

  lazy val refLine1: Ref[LineText] = Ref[LineText](dataType = entityTypeL, uuid = line1uuid)
  lazy val refLine2                = Ref[LineText](dataType = entityTypeL, uuid = line2uuid)
  lazy val refLine3                = Ref[LineText](dataType = entityTypeL, uuid = line3uuid)

  lazy val lwq1 = LineWithQue(line = line1)
  lazy val lwq2 = LineWithQue(line = line2)
  lazy val lwq3 = LineWithQue(line = line3)

  def f(l:LineWithQue,i:Int):Ref[LineWithQue]= Ref[LineWithQue](dataType = TypeAsString.make[LineWithQue],
                                                          uuid="00000000-0000-0000-0000-00000000000"+i)

  lazy val list = UserLineList(user = userRef,
                               name = "testname",
                               lines = List(f(lwq1,1), f(lwq2,2), f(lwq3,3)))

  lazy val listRefUuid                = "20000000-0000-0000-0000-000000000001"
  lazy val listRef: Ref[UserLineList] = Ref[UserLineList](uuid= listRefUuid, dataType = TypeAsString.make[UserLineList])

  lazy val listRV = RefVal(listRef,list,Version())
}