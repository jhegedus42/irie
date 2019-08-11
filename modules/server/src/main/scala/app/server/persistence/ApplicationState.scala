package app.server.persistence

import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.EntityTypeAsString
import app.shared.dataModel.entity.refs.TypedRefVal

import scala.reflect.ClassTag

case class EntityUUID(uuid:EntityUUID)
case class UntypedRef(uuid:String,version:Long,entityType:EntityTypeAsString)

case class ApplicationState(stateMap: Map[UntypedRef, TypedRefVal[_]] = Map.empty ) {



  def insertEntity(untypedRef: UntypedRef ): ApplicationState = {
//    this.copy( stateMap = this.stateMap + (refValDyn.r -> refValDyn))
    ??? // todo-next-1
  }

//  def updateEntity(refValDyn: RefValDyn ) : Option[ApplicationState]  = {
//
//    if (!stateMap.contains(refValDyn.r )){
//      return Some(this.copy( stateMap = this.stateMap + (refValDyn.r -> ) ) )
//
//    }
//  }


  def getEntity[E <: Entity[E]: ClassTag](r: UntypedRef ): Option[TypedRefVal[_]] = {
    this.stateMap.get( r )
  }
}
