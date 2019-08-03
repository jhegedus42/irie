package app.testHelpersServer.state

import app.server.persistence.ApplicationState
import app.shared.SomeError_Trait
import app.shared.data.model.Note
import app.shared.data.ref.{RefVal, Version}
import app.shared.data.utils.PrettyPrint
import app.testHelpersShared.data.TestDataLabels.{LabelOne, LabelThree, LabelTwo, TestDataLabel}
import scalaz.\/


  object TestData {

    def getTestDataFromLabels(label:TestDataLabel):ApplicationState ={
      label match {
        case LabelOne => TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        case LabelTwo => TestState_LabelTwo_OneLine_WithVersionOne_nothing_else
      }
    }

    import app.testHelpersShared.data.TestEntities.refValOfLineV0


    val TestState_LabelOne_OneLine_WithVersionZero_nothing_else:  ApplicationState =
      ApplicationState().insertEntity(refValOfLineV0).toEither.right.get



    object LabelOneEntities{
      val lineInState: RefVal[Note] = refValOfLineV0
    }




    val TestState_LabelTwo_OneLine_WithVersionOne_nothing_else:  ApplicationState = {
      val res = TestState_LabelOne_OneLine_WithVersionZero_nothing_else.updateEntity(refValOfLineV0)
      res.toEither.right.get._1
    }



}


