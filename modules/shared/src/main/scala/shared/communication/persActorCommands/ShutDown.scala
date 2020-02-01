package shared.communication.persActorCommands

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

case object ShutDown extends PersActorQuery
