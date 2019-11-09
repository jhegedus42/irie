package refs

//import entity.view.{View, ViewSet}
//import monocle.macros.Lenses

//import io.circe.generic.auto._
//import io.circe.syntax._
import io.circe.generic.JsonCodec


//@Lenses

@JsonCodec
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
        .groupBy(_.toRef.entityIdentity.uuid).transform({
          (x, y) =>
            y.maxBy(_.toRef.entityVersion.versionNumberLong)
        }).values.toSet
    )
  }

}


