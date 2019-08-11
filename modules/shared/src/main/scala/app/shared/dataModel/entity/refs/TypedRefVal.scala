package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity.Entity
import monocle.macros.Lenses

case class Version(l: Long = 0 ) {
  def inc(): Version = this.copy( l = this.l + 1 )
}

case class DeletedFlag(isDeleted:Boolean)

@Lenses
case class TypedRefVal[T <: Entity[T]](r: TypedRef[T], v: T, version: Version, isDeleted:DeletedFlag = DeletedFlag(false) ) {

//    def makeWithNewUUID (v: T)(implicit t: ClassTag[T]): TypedRefVal[T] =
//      new TypedRefVal(TypedRef.make[T](), v, Version())

  def map(f: T => T ): TypedRefVal[T] = copy( v = f( v ) )

  override def toString: String = super.toString

//  override def hashCode(): Int = 1013 * r.hashCode() ^ 1009 * version.hashCode()

}

object TypedRefVal {

//  implicit def instance[T <: Entity[T]]: UUIDCompare[TypedRefVal[T]] =
//    (x: TypedRefVal[T], y: TypedRefVal[T]) => TypedRef.instance.isUUIDEq(x.r, y.r)

}

