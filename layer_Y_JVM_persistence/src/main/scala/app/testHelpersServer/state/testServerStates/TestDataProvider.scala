package app.testHelpersServer.state.testServerStates

import app.server.persistence.ApplicationState
import app.shared.data.model.User
import app.testHelpersShared.data.{TestDataForUsers, TestDataLabel}

trait TestDataProvider[T<:TestDataLabel] {
  def getTestData: app.server.persistence.ApplicationState

}

object TestDataProvider {
  object TestDataForUsersInstance extends TestDataProvider[TestDataForUsers.type ]{
    // todo add 3 users
    val alice=User("Alice")
    val bob=User("Bob")

    override def getTestData: ApplicationState = ???
  }

}




