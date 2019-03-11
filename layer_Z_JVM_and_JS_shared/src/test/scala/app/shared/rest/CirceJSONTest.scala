package app.shared.rest

import app.shared.data.model.TypeAsString
import app.shared.data.ref.{RefVal, RefValTestUtil}
import org.scalatest.{FunSuite, Matchers}



class CirceJSONTest extends FunSuite with Matchers {
  import app.shared.data.model.LineText
  import app.shared.data.ref.Ref
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._

  import scala.util.Right

  object RefValFix{

    val l: LineText          = LineText(title = "lt1",text="text")
    val rv: RefVal[LineText] = RefValTestUtil.makeWithNewUUID(l)
  }
  test("Decode Ref WithOut TypeCheck") {
    val r: Ref[LineText] = Ref.make[LineText]()
    val r2: Ref[LineText] = Ref.make[LineText]()
    val rs: String = r.asJson.spaces4

    val decoded: Either[Error, Ref[LineText]] = decode[Ref[LineText]](rs)
    assert(1 == 1)
    decoded shouldEqual Right(r)
    decoded should not equal Right(r2)
  }

  test("Decode RefVal WithOut TypeCheck") {
    val l: LineText =LineText(title = "lt1",text="text")
    val rv: RefVal[LineText] = RefValTestUtil.makeWithNewUUID(l)
    val rv2: RefVal[LineText] = RefValTestUtil.makeWithNewUUID(l)
    val rs: String = rv.asJson.spaces4
    println(rv)

    val decoded: Either[Error, RefVal[LineText]] = decode[RefVal[LineText]](rs)
    assert(1 == 1)
    decoded shouldEqual Right(rv)
    decoded should not equal Right(rv2)
  }

  test("Decode incorrect RefVal With TypeCheck should be left") {
    val l: LineText = LineText(title = "lt1",text="text")
    import monocle.macros.syntax.lens._

    val ltemp: RefVal[LineText] = RefValTestUtil.makeWithNewUUID(l)
    val rv2_wrong_type: RefVal[LineText] = ltemp.lens(_.r.dataType).set(TypeAsString("bla"))
    val rs: String = rv2_wrong_type.asJson.spaces4

    println(rv2_wrong_type)

    import CirceJSON.typedRefValDecode
    val decoded: Either[Error, RefVal[LineText]] = typedRefValDecode[LineText](rs)
    assert(decoded.isLeft)
    println(decoded)
  }

  test("Decode correct RefVal With TypeCheck should be right") {
    val l: LineText = LineText(title = "lt1",text="text")

    val rv2: RefVal[LineText] = RefValTestUtil.makeWithNewUUID(l)
    val rs: String = rv2.asJson.spaces4

    println(rv2)

    import CirceJSON.typedRefValDecode
    val decoded: Either[Error, RefVal[LineText]] = typedRefValDecode[LineText](rs)
    decoded === Right(rv2)
    println(decoded)
  }



}
