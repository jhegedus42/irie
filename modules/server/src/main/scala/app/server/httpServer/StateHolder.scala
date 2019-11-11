package app.server.httpServer

import comm.crudRequests.Req
import dataStorage.normalizedDataModel.EntityValueType

import scala.concurrent.Future

case class StateHolder() {

  def handleCDUDRequest[
    RT <: Req[E],
    E  <: EntityValueType[E]
  ](r: RT#Par[E]): Future[RT#Resp] = ???
 // todo-later we need a nice type class here ...
}

