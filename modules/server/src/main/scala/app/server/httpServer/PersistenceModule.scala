package app.server.httpServer

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity
import app.shared.dataModel.model.User

import scala.concurrent.Future

case class PersistenceModule() {

  def getEntity[E<:EntityValue[E]](uuid:String)    :  Future[Entity[E]] = ???

  def createNewEntity[E<:EntityValue[E]](entity: EntityValue[E]): Future[Entity[E]] = ???

  def init(): Unit ={

    val alice= User("Alice",38)
    val bob= User("Bob",45)
    val kutya= User("Kutya",42)
    val cica= User("Cica",137)
    val meresiHiba= User("MeresiHiba",369)

    //  step 1 - csinaljunk beloluk valamit amit bele lehet(todo-now)
    //   tenni
    //
    //     - de mit eszik a Journal ? => ki kell talalni
    //       - hol van a Journal ?
    //       - figure out how to store the entities in the application state
    //       - sima Entity -kent casting-gal
    //  step 2 - tegyuk bele a journal-ba / application state-be
    //  step 3 - olvassuk ki az application state tartalmat es irjuk
    //           ki a konzolra

  }

}
