package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{RefToEntityWithVersion }
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
case class AdminUser(ref: RefToEntityWithVersion[User])
    extends EntityValue[User]
