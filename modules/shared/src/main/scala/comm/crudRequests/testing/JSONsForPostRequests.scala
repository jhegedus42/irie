package comm.crudRequests.testing

import comm.crudRequests.{GetAllEntityiesForUser, JSONConvertable}
import dataStorage.{Ref, User, UserRef}
import testingData.TestDataStore

object JSONsForPostRequests extends App{
 lazy val userRef  =TestDataStore.aliceEnt.ref.unTypedRef.userRef
 val req= GetAllEntityiesForUser(userRef,None)
 // continu here, print this json
 val i =implicitly[JSONConvertable[GetAllEntityiesForUser]]
  val j: String =i.getJSON(req)
 println(j)

}
