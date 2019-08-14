package app.shared.dataModel.value.refs

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.ValueTypeAsString
import app.shared.utils.UUID_Utils.{IdentityOfEntity}
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class TypedRefToEntity[T <: EntityValue[T]](
                                    valueTypeAsString: ValueTypeAsString,
                                    entityUUID:        IdentityOfEntity = IdentityOfEntity(),
                                    version:           EntityVersion = EntityVersion()
)

object TypedRefToEntity {

  def getDefaultValue[T <: EntityValue[T]](implicit t: ClassTag[T] ): TypedRefToEntity[T] =
    TypedRefToEntity[T]( ValueTypeAsString.make[T] )
}
