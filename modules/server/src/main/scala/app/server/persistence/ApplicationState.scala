package app.server.persistence

import app.server.persistence.ApplicationState.RefValDyn
import app.shared.entity.Entity.Entity
import app.shared.entity.{TypedRef, TypedRefVal}

import scala.reflect.ClassTag


case class ApplicationState(stateMap: Map[TypedRef[_], TypedRefVal[_]] = Map.empty ) {

  case class StateUpdateError(s: String )

  def insertEntity(refValDyn: RefValDyn ): ApplicationState = {
//    this.copy( stateMap = this.stateMap + (refValDyn.r -> refValDyn))
    // todo fix this ??? below
    ???
  }

//  def updateEntity(refValDyn: RefValDyn ) : Option[ApplicationState]  = {
//
//    if (!stateMap.contains(refValDyn.r )){
//      return Some(this.copy( stateMap = this.stateMap + (refValDyn.r -> ) ) )
//
//    }
//  }


  def getEntity[E <: Entity[E]: ClassTag](r: TypedRef[E] ): Option[TypedRefVal[_]] = {
    this.stateMap.get( r )
  }
}

object ApplicationState {
  type RefValDyn=TypedRefVal[_]
}
