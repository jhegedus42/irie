package app.server.testData

import app.server.persistence.ApplicationState
import app.shared.dataModel.entity.testData.{TestDataForUsers, TestDataLabel}

trait TestDataProvider[T<:TestDataLabel] {
  def getTestData: ApplicationState

}

object TestDataProvider {
  object TestDataForUsersInstance extends TestDataProvider[TestDataForUsers.type ]{
//     todo add 3 users
//    def TypedRef
//    val alice=TypedRefVal.make (User("Alice"))
//    val bob=User("Bob")
//    val cecile=User("Cecile")
//
//    val ct=implicitly[ClassTag[User]]
//    NoteFolder(user=TypedRef.make(alice),name="Friends")

    override def getTestData: ApplicationState = ???
  }

}




