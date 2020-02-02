package shared.communication.persActorCommands.auth

import io.circe.{Decoder, Encoder}
import org.scalatest.FunSuite
import shared.communication.CanProvideRouteName
import shared.communication.persActorCommands.{Query, Response}
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd
import shared.dataStorage.model.PWDNotHashed
import shared.testingData.TestDataStore

class QueryAuthWrapperTest extends FunSuite {

  test("test Query Encoding and Decoding") {
    val refToEntityOwningUser =
      TestDataStore.aliceUserEnt.ref.unTypedRef.refToEntityOwningUser

    val cmd =
      GetAllEntityiesForUserPersActCmd(refToEntityOwningUser, None)

    val pwd   = PWDNotHashed("titok")
    val query = QueryAuthWrapper(cmd, pwd)
    import io.circe.parser._

  }


}
