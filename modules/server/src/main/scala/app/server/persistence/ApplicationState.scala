package app.server.persistence

import app.server.persistence.ApplicationState.RefValDyn
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{TypedRef, TypedRefVal}

import scala.reflect.ClassTag


case class ApplicationState(stateMap: Map[TypedRef[_], TypedRefVal[_]] = Map.empty ) {

  case class StateUpdateError(s: String )

  def insertEntity(refValDyn: RefValDyn ): ApplicationState = {
    this.copy( stateMap = this.stateMap + (refValDyn.r -> refValDyn))
  }

//  def updateEntity(refValDyn: RefValDyn ): \/[SomeError_Trait, ( ApplicationState, RefValDyn )] = {
//
//    if (!stateMap.contains( rr ))
//      return -\/(
//        EntityIsNotUpdateableError(
//          "entity does not exist",
//          Some( EntityDoesNotExistError( "while updating State" ) )
//        )
//      )
//
//    if (refValDyn.version == stateMap( refValDyn.r ).version) {
//      val newVal = refValDyn.copy( version = refValDyn.version.inc() )
//      return \/-( this.copy( stateMap = this.stateMap + (refValDyn.r -> newVal) ), newVal )
//    } else {
//      val r = -\/(
//        InvalidVersionError( "while updating the state", stateMap( refValDyn.r ).version,
//                             refValDyn.version )
//      )
//      return r
//    }
//
//  }


  def getEntity[E <: Entity[E]: ClassTag](r: TypedRef[E] ): Option[TypedRefVal[_]] = {
    this.stateMap.get( r )
  }
}

object ApplicationState {
  type RefValDyn=TypedRefVal[_]
}
