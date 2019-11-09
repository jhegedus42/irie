package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.{EntityType, RefToEntityWithVersion}

@JsonCodec
case class AdminUser(ref: RefToEntityWithVersion[User])
    extends EntityType[User]
