package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion

case class Note(
  content: String,
  owner:   RefToEntityWithVersion[User])
    extends EntityType[Note]
