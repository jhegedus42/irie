package app.shared.entity.collection

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.view.{View, ViewSet}

case class EntitySet[T <: EntityType[T]](set: Set[EntityWithRef[T]]) {

  def filterToLatestVersions: EntitySet[T] = {
    EntitySet(
      set
        .groupBy(_.refToEntity.entityIdentity.uuid).transform({
          (x, y) =>
            y.maxBy(_.refToEntity.entityVersion.versionNumberLong)
        }).values.toSet
    )
  }

  def joinWith[U <: EntityType[U], V <: View](
    other: EntitySet[U]
  )(f:     (T, U) => V
  ): ViewSet[V] = {
    ???
  }

}
