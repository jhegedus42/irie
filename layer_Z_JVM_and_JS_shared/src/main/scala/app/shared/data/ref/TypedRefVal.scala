package app.shared.data.ref

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.UUID_Utils.UUIDCompare
import monocle.macros.Lenses
//import shapeless.Typeable

case class Version(l: Long = 0 ) {
  def inc(): Version = this.copy( l = this.l + 1 )
}

case class DeletedFlag(isDeleted:Boolean)

/**
  * Created by joco on 28/04/2017.
  */
@Lenses
case class TypedRefVal[T <: Entity[T]](r: TypedRef[T], v: T, version: Version, isDeleted:DeletedFlag = DeletedFlag(false) ) {
  def toRefVal = ???


  def map(f: T => T ): TypedRefVal[T] = copy( v = f( v ) )

  override def toString: String = super.toString

  override def hashCode(): Int = 1013 * r.hashCode() ^ 1009 * version.hashCode()

}

object TypedRefVal {

  implicit def instance[T <: Entity[T]]: UUIDCompare[TypedRefVal[T]] =
    (x: TypedRefVal[T], y: TypedRefVal[T]) => TypedRef.instance.isUUIDEq(x.r, y.r)

}

