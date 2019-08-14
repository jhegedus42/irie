package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class TypedRefVal[E <: Entity[E]](
    entity:      E,
    typedRef:    TypedRef[E],
    version:     Version = Version(),
    deletedFlag: DeletedFlag = DeletedFlag( false )
) {

  def toJSON( implicit e: Encoder[TypedRefVal[E]] ): String =
    e.apply( this ).spaces4

}

object TypedRefVal {

  def makeFromEntity[E <: Entity[E]: ClassTag]( v: E ): TypedRefVal[E] =
    TypedRefVal( v, TypedRef.getDefaultValue[E] )

}
