package app.server.httpServer

import app.shared.dataModel.entity.Entity
import app.shared.dataModel.entity.refs.TypedRefVal

import scala.concurrent.Future

case class PersistenceModule() {

  def getEntity[E<:Entity[E]](uuid:String) :Future[TypedRefVal[E]] = ???

  def init(): Unit ={

    //  todo now  - step 0 - csinaljunk egy Alice-t

    //  step 1 - csinaljunk belole Entity-t
    //  step 1 - tegyuk bele a journal-ba / application state-be
    //  step 2 - olvassuk ki az application state tartalmat es irjuk
    //           ki a konzolra

  }

}
