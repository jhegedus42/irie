package dataModel

import refs.RefToEntityWithVersion
import refs.entityValue.EntityType
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
case class AdminUser(ref: RefToEntityWithVersion[User])
    extends EntityType[User]
