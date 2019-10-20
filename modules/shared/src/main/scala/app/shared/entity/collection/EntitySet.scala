package app.shared.entity.collection

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType

case class EntitySet[T<:EntityType[T]](set:Set[EntityWithRef[T]])
