package sandbox.cats

class Ch_01_01 {

  // Define a very simple JSON AST
  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  case object JsNull extends Json
  // The "serialize to JSON" behaviour is encoded in this trait
  trait JsonWriter[A] {
    def write(value: A): Json
  }

}
