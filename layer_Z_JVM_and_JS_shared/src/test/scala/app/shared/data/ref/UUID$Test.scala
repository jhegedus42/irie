package app.shared.data.ref

import app.shared.{InvalidUUIDinURLError, StateOpsError}
import app.shared.data.ref.uuid.UUID
import org.scalatest.FunSuite

import scalaz.\/

/**
  * Created by joco on 15/05/2017.
  */
class UUID$Test extends FunSuite {

  test("testValidate_from_String happy path") {
    val s = "01234567-9ABC-DEF0-1234-56789ABCDEF0"
    val r: \/[InvalidUUIDinURLError, UUID] = UUID.validate_from_String(s)
    assert(r.isRight)
  }
  test("testValidate_from_String sad path") {
    val s = "01234567-9ABC-DEF0-12434-56789ABCDEF0"
    val r: \/[InvalidUUIDinURLError, UUID] = UUID.validate_from_String(s)
    assert(r.isLeft)
  }

}
