package app.shared

import app.shared.data.ref.Version

sealed trait SomeError_Trait {
  val string: String
}

case class EntityIsNotUpdateableError(string: String, edne: Option[SomeError_Trait] = None )
    extends SomeError_Trait

case class EntityIsNotCreateableError(string: String, edne: Option[SomeError_Trait] = None )
    extends SomeError_Trait

case class EntityDoesNotExistError(string: String ) extends SomeError_Trait

case class InvalidUUIDinURLError(string: String ) extends SomeError_Trait

case class InvalidViewParamsError(string: String ) extends SomeError_Trait

case class StateOpsError(string: String ) extends SomeError_Trait

case class InvalidVersionError(string: String, expected: Version, actual: Version ) extends SomeError_Trait

case class TypeError(string: String ) extends SomeError_Trait

case class CirceDecoderImplicitNotFoundTest(t: String )

object SomeError_Trait {

  import io.circe._, io.circe.generic.semiauto._, io.circe.generic.auto._
// ez azert van itt hogy a SomeError_Trait dekodolasahoz legyen mindig scope-ban implicit val
  implicit val fooDecoder: Decoder[SomeError_Trait] = deriveDecoder[SomeError_Trait]
  implicit val fooEncoder: Encoder[SomeError_Trait] = deriveEncoder[SomeError_Trait]
}
