package app.server.httpServer.persistence.state

import app.shared.dataModel.value.EntityValue

import scala.reflect.ClassTag

case class ApplicationState(
    stateMap: Map[UntypedRef, ApplicationStateEntry] = Map.empty
) {

  def insertNewApplicationStateEntry(
      untypedRef: UntypedRef
  ): ApplicationState = {
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

  def getEntity[E <: EntityValue[E]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateEntry] = {
    this.stateMap.get( r )
  }
}
