package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

@JsonCodec
case class AdminUser(ref: RefToEntityWithoutVersion[User])
    extends EntityValue[User]
