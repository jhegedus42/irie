package app.server.persistence.state

import app.shared.dataModel.entity.Entity

import scala.reflect.ClassTag

case class EntityAsJSON(json:String)

case class ApplicationStateEntry(
    untypedRef:    UntypedRef,
    entityAsJSON: EntityAsJSON
)

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

  def getEntity[E <: Entity[E]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateEntry] = {
    this.stateMap.get( r )
  }
}
