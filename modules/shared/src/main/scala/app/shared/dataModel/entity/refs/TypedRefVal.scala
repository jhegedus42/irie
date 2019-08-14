package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

case class Version( l: Long = 0 ) {
  def inc(): Version = this.copy( l = this.l + 1 )
}

case class DeletedFlag( isDeleted: Boolean )

@Lenses
case class TypedRefVal[E <: Entity[E]](
    e:         E,
    r:         TypedRef[E],
    version:   Version = Version(),
    isDeleted: DeletedFlag = DeletedFlag( false )
) {

  def toJSON( implicit e: Encoder[TypedRefVal[E]] ): String =
    e.apply( this ).spaces4

  def makeFromEntity( v: E )( implicit classTag: ClassTag[E] ): TypedRefVal[E] = {
    TypedRefVal( v, TypedRef.getDefaultValue[E] )
  }
}
