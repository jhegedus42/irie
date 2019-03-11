package app.testHelpersServer.state

import app.server.persistence.ApplicationState
import app.shared.SomeError_Trait
import app.shared.data.model.LineText
import app.shared.data.ref.{RefVal, Version}
import app.shared.data.utils.PrettyPrint
import app.testHelpersShared.data.TestDataLabels.{LabelOne, LabelThree, LabelTwo, TestDataLabel}
import scalaz.\/


  object TestData {

    def getTestDataFromLabels(label:TestDataLabel):ApplicationState ={
      label match {
        case LabelOne => TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        case LabelTwo => TestState_LabelTwo_OneLine_WithVersionOne_nothing_else
        case LabelThree => StateThree.state
      }
    }

    import app.testHelpersShared.data.TestEntities.refValOfLineV0


    val TestState_LabelOne_OneLine_WithVersionZero_nothing_else:  ApplicationState =
      ApplicationState().insertEntity(refValOfLineV0).toEither.right.get



    object LabelOneEntities{
      val lineInState: RefVal[LineText] = refValOfLineV0
    }




    val TestState_LabelTwo_OneLine_WithVersionOne_nothing_else:  ApplicationState = {
      val res = TestState_LabelOne_OneLine_WithVersionZero_nothing_else.updateEntity(refValOfLineV0)
      res.toEither.right.get._1
    }


  }

object StateThree extends App {
  import app.testHelpersShared.data.TestEntitiesForStateThree._

  def step1= ApplicationState()
  def step2: \/[SomeError_Trait, ApplicationState] = {
    step1.insertEntity(rvdUser)
  }

  def s6val(): \/[SomeError_Trait, ApplicationState] = for {
    s2 <- ApplicationState().insertEntity(rvdUser)
    s3 <- s2.insertEntity(RefVal(refLine1, line1, Version()))
    s4 <- s3.insertEntity(RefVal(refLine2, line2, Version()))
    s5 <- s4.insertEntity(RefVal(refLine3, line3, Version()))
    s6 <- s5.insertEntity(RefVal(listRef, list, Version()))
  } yield (s6)

  def state: ApplicationState = {
    val s= s6val()
    val res=s.toEither.right.get
    res
  }
  def printState() = {
    lazy val res: String = PrettyPrint.prettyPrint(state.stateMap.toList)
  }

}


