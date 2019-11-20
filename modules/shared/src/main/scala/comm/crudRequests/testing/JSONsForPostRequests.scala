package comm.crudRequests.testing

import comm.crudRequests.JSONConvertable
import comm.crudRequests.persActorCommands.GetAllEntityiesForUser
import dataStorage.{Ref, RefToEntityOwningUser, User}
import testingData.TestDataStore

object JSONsForPostRequests extends App {

  lazy val userRef =
    TestDataStore.aliceEnt.ref.unTypedRef.refToEntityOwningUser
  val req = GetAllEntityiesForUser(userRef, None)
  // continu here, print this json
  val i = implicitly[JSONConvertable[GetAllEntityiesForUser]]
  val j: String = i.getJSON(req)
  println(j)

}
