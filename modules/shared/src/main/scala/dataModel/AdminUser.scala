package dataModel

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import entity.{EntityType, IdentityAndVersion}

@JsonCodec
case class AdminUser(ref: IdentityAndVersion[User])
    extends Entity[User]
