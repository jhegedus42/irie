package shared.crudRESTCallCommands.testing

import shared.crudRESTCallCommands.JSONConvertable
import shared.crudRESTCallCommands.persActorCommands.GetAllEntityiesForUserPersActCmd
import shared.testingData.TestDataStore

object JSONsForPostRequests extends App {

  lazy val userRef =
    TestDataStore.aliceUserEnt.ref.unTypedRef.refToEntityOwningUser
  val req = GetAllEntityiesForUserPersActCmd(userRef, None)

  // continu here, print this json
  val i =
    implicitly[JSONConvertable[GetAllEntityiesForUserPersActCmd]]
  val j: String = i.toJSON(req)
  println(j)

}
