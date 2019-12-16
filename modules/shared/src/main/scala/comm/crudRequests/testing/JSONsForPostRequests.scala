package comm.crudRequests.testing

import comm.crudRequests.JSONConvertable
import comm.crudRequests.persActorCommands.GetAllEntityiesForUser
import shared.testingData.TestDataStore

object JSONsForPostRequests extends App {

  lazy val userRef =
    TestDataStore.aliceEnt.ref.unTypedRef.refToEntityOwningUser
  val req = GetAllEntityiesForUser(userRef, None)

  // continu here, print this json
  val i =
    implicitly[JSONConvertable[GetAllEntityiesForUser]]
  val j: String = i.toJSON(req)
  println(j)

}
