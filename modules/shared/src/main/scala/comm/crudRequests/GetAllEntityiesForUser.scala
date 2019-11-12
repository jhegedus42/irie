package comm.crudRequests

import dataStorage.UserRef
import dataStorage.stateHolder.UserMap
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._


@JsonCodec
case class GetAllEntityiesForUser(
  par: UserRef,
  res: Option[UserMap])


