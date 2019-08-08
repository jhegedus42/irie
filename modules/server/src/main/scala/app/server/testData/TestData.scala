package app.server.testData

import app.server.persistence.ApplicationState
import app.shared.testData.{LabelOne, TestDataLabel}


object TestData {

  def getTestDataFromLabels(label: TestDataLabel): ApplicationState = {
    label match {
      case LabelOne => ApplicationState().insertEntity(refValOfLineV0)
    }
  }



}


