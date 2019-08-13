package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity
import monocle.macros.Lenses
import io.circe.{Decoder, Error, _}

case class Version(l: Long = 0 ) {
  def inc(): Version = this.copy( l = this.l + 1 )
}

case class DeletedFlag(isDeleted:Boolean)

@Lenses
case class TypedRefVal[T <: Entity[T]](r: TypedRef[T], v: T, version: Version, isDeleted:DeletedFlag = DeletedFlag(false) ) {

  def toJSON(implicit e:Encoder[TypedRefVal[T]]):String = e.apply(this).spaces4

}


