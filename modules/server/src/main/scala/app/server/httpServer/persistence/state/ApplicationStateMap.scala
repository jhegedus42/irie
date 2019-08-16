package app.server.httpServer.persistence.state

import app.shared.dataModel.value.EntityValue

import scala.reflect.ClassTag

case class ApplicationStateMap(
    stateMap: Map[UntypedRef, ApplicationStateEntry] = Map.empty
) {

  def insertNewApplicationStateEntry( ase: ApplicationStateEntry ): Option[ApplicationStateMap] = {
    if (!stateMap.contains( ase.untypedRef )) {
      val res = this.copy( stateMap = this.stateMap + (ase.untypedRef -> ase) )
      Some( res )
    } else None

  }

  def getEntity[E <: EntityValue[E]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateEntry] = {
    this.stateMap.get( r )
  }
}
