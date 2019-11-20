package comm.crudRequests.persActorCommands

import comm.crudRequests.{CanProvideRouteName, JSONConvertable}
import dataStorage.RefToEntityOwningUser
import dataStorage.stateHolder.UserMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

case object ShutDown extends PersActorCommand
