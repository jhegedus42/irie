package app.server.testData

import app.shared.data.model.Note
import app.shared.data.ref.TypedRefVal
import app.testHelpersShared.data.{LabelOne, LabelTwo, TestDataLabel}


object TestData {

  import app.testHelpersShared.data.TestEntities.refValOfLineV0

  val TestState_LabelOne_OneLine_WithVersionZero_nothing_else: ApplicationState =
    ApplicationState().insertEntity(refValOfLineV0).toEither.right.get

  val TestState_LabelTwo_OneLine_WithVersionOne_nothing_else: ApplicationState = {
    val res = TestState_LabelOne_OneLine_WithVersionZero_nothing_else.
      updateEntity(refValOfLineV0)
    res.toEither.right.get._1
  }

  def getTestDataFromLabels(label: TestDataLabel): ApplicationState = {
    label match {
      case LabelOne => TestState_LabelOne_OneLine_WithVersionZero_nothing_else
      case LabelTwo => TestState_LabelTwo_OneLine_WithVersionOne_nothing_else
    }
  }

  object LabelOneEntities {
    val lineInState: TypedRefVal[Note] = refValOfLineV0
  }


}


