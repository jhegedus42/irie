package app.testHelpersServer.state.testServerStates

import app.server.persistence.ApplicationState
import app.shared.data.model.{NoteFolder, User}
import app.shared.data.ref.{TypedRef, TypedRefVal}
import app.testHelpersShared.data.{TestDataForUsers, TestDataLabel}

import scala.reflect.ClassTag

trait TestDataProvider[T<:TestDataLabel] {
  def getTestData: app.server.persistence.ApplicationState

}

object TestDataProvider {
  object TestDataForUsersInstance extends TestDataProvider[TestDataForUsers.type ]{
//     todo add 3 users
//    def TypedRef
//    val alice=TypedRefVal.make (User("Alice"))
    val bob=User("Bob")
    val cecile=User("Cecile")
//
//    val ct=implicitly[ClassTag[User]]
//    NoteFolder(user=TypedRef.make(alice),name="Friends")

    override def getTestData: ApplicationState = ???
  }

}




