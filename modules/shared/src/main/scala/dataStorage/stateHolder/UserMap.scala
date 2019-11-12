package dataStorage.stateHolder

import io.circe.Json
import io.circe.generic.JsonCodec
import scala.collection.Map

@JsonCodec
case class UserMap(ref : List[((String, String), Json)])
