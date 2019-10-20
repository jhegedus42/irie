package app.shared.entity.collection

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
//import app.shared.entity.view.{View, ViewSet}

case class UsersLatestVersionEntitySet[T <: EntityType[T]](
  userRef:                EntityWithRef[User],
  latestVersionEntitySet: LatestVersionEntitySet[T])

case class LatestVersionEntitySet[T <: EntityType[T]](
  set: Set[EntityWithRef[T]]) {

//  def joinWith[U <: EntityType[U], V <: View](
//    other: LatestVersionEntitySet[U]
//  )(f:     (EntityWithRef[T], EntityWithRef[U]) => Option[V]
//  ): ViewSet[V] = {
//    val res: Set[Option[V]] = for {
//      a <- set
//      b <- other.set
//      c = f(a, b)
//    } yield c
//    ViewSet(res.filter(_.isDefined).map(_.get))
//  }

  def filter(f:(EntityWithRef[T])=>Boolean): LatestVersionEntitySet[T] ={
    LatestVersionEntitySet(set.filter(f))
  }

}

case class EntitySet[T <: EntityType[T]](set: Set[EntityWithRef[T]]) {

  def filterToLatestVersions: LatestVersionEntitySet[T] = {
    LatestVersionEntitySet(
      set
        .groupBy(_.refToEntity.entityIdentity.uuid).transform({
          (x, y) =>
            y.maxBy(_.refToEntity.entityVersion.versionNumberLong)
        }).values.toSet
    )
  }

}
