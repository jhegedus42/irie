package app.shared.data.ref

import app.shared.data.model.Entity.Entity
import org.scalatest.FunSuite

import scala.reflect.ClassTag

/**
  * Created by joco on 09/09/2017.
  */
class TypedRefTest extends FunSuite {

}
object RefValTestUtil {
  def makeWithNewUUID[T <: Entity](v: T)
                                (implicit t: ClassTag[T]) : RefVal[T] = new RefVal(TypedRef.make[T](), v,Version())
}
