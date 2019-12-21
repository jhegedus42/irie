package shared.dataStorage.stateHolder

import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.{
  RefToEntityOwningUser,
  UnTypedRef,
  UnTypedReferencedValue
}

@JsonCodec
case class UserMap(
  user: RefToEntityOwningUser        = RefToEntityOwningUser(),
  list: List[UnTypedReferencedValue] = List())
