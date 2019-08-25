package app.shared.entity

import app.shared.entity.asString.{EntityAsJSON, EntityAsString, EntityValueAsToString, EntityValueTypeAsString}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{EntityDeletedFlag, RefToEntityWithVersion}
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class Entity[E <: EntityValue[E]](
                                        entityValue:       E,
                                        refToEntity:       RefToEntityWithVersion[E],
                                        entityDeletedFlag: EntityDeletedFlag = EntityDeletedFlag( false )
) {

  private def toJSON( implicit e: Encoder[Entity[E]] ): EntityAsJSON = {
    val jsonAsString: String = e.apply( this ).spaces4
    val res:          Json   = e.apply( this )
    EntityAsJSON( res )
  }

  private def valueAsToString(): EntityValueAsToString = {
    EntityValueAsToString( this.entityValue.toString )
  }

  def entityAsString()( implicit e: Encoder[Entity[E]] ): EntityAsString = {
    EntityAsString( this.toJSON, this.valueAsToString() )
  }

}

object Entity {

  def makeFromValue[V <: EntityValue[V]: ClassTag]( v: V ): Entity[V] = {
    val tr = RefToEntityWithVersion[V]( EntityValueTypeAsString.make[V] )
    Entity( v, tr )
  }


}

