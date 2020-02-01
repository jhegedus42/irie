package shared.communication.testing

import shared.communication.JSONConvertable
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd
import shared.testingData.TestDataStore

//object JSONsForPostRequests extends App {
//
//  lazy val userRef =
//    TestDataStore.aliceUserEnt.ref.unTypedRef.refToEntityOwningUser
//  val req = GetAllEntityiesForUserPersActCmd(userRef, None)
//
//  // continu here, print this json
//  val i =
//    implicitly[JSONConvertable[GetAllEntityiesForUserPersActCmd]]
//  val j: String = i.toJSON(req)
//  println(j)
//
//}
