package app.server.testData

import app.server.persistence.ApplicationState
import app.shared.testData.{LabelOne, TestDataLabel, TestEntities}


object TestData {

  def getTestDataFromLabels(label: TestDataLabel): ApplicationState = {
    label match {
      case LabelOne => ApplicationState().insertEntity(TestEntities.refValOfLineV0)
    }
  }



}


