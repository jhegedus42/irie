package app.server.testData

import app.server.persistence.ApplicationState
import app.shared.dataModel.entity.testData.{LabelOne, TestDataLabel, TestEntities}


object TestData {

  def getTestDataFromLabels(label: TestDataLabel): ApplicationState = {
    label match {
//      case LabelOne => ApplicationState().insertEntity(TestEntities.refValOfLineV0)
        case _ => ??? // todo fix this ???

    }
  }



}


