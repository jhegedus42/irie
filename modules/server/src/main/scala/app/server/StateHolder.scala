package app.server

import app.server.httpServer.routes.SodiumCRUDRoute
import dataModel.EntityValueType
import entity.Ref
import syncedNormalizedState.comm.SodiumCRUDReq

import scala.concurrent.Future

case class StateHolder() {

  def handleCDUDRequest[
    RT <: SodiumCRUDReq[E],
    E  <: EntityValueType[E]
  ](r: RT#Par
  ): Future[RT#Resp] = ???
 // todo-later we need a nice type class here ...
}

