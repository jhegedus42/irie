package app.client.ui.caching.localState

import app.client.ui.components.router.mainPageComp.cacheTestMPC.AddTheThieveryNumbersUsingTheServer.TheThieveryNumber
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.TypedRef

import scala.reflect.ClassTag
import scala.collection.immutable.HashMap

trait ClientStateEntity extends Entity

case class TypedRefToClientState[CSE <: ClientStateEntity: ClassTag](
    tr: TypedRef[CSE])

case class ClientStateVal[CSE <: ClientStateEntity: ClassTag](
    typedRefToClientState: TypedRefToClientState[CSE],
    cse:                   ClientStateEntity)

object TypedRefToClientState {
  def makeNew[
      CSE <: ClientStateEntity: ClassTag
    ]: TypedRefToClientState[CSE] = {
    val tr:   TypedRef[CSE] = TypedRef.make[CSE]
    val trcs: TypedRefToClientState[CSE] = TypedRefToClientState( tr )
    trcs
  }
}

trait ClientSideStateContainingMap[CSE <: ClientStateEntity] {

  sealed trait DidUpdateWork
  object Success extends DidUpdateWork
  object Failure extends DidUpdateWork

  private var map: HashMap[TypedRefToClientState[CSE], ClientStateVal[CSE]] =
    new HashMap[TypedRefToClientState[CSE], ClientStateVal[CSE]]()

  def addNewValue(
      clientStateEntity: CSE
    )(implicit classTag: ClassTag[CSE]
    ) = {
    val tr: TypedRefToClientState[CSE] = TypedRefToClientState.makeNew[CSE]
    val clientStateVal: ClientStateVal[CSE] =
      ClientStateVal( tr, clientStateEntity )
    val keyValue
        : ( TypedRefToClientState[CSE], ClientStateVal[CSE] ) = (tr -> clientStateVal)
    val newMap = map + keyValue
    map = newMap
  }

  def update(clientStateVal: ClientStateVal[CSE] ): DidUpdateWork = {
    if (map.contains( clientStateVal.typedRefToClientState )) {
      val key: TypedRefToClientState[CSE] = clientStateVal.typedRefToClientState
      val keyValue
          : ( TypedRefToClientState[CSE], ClientStateVal[CSE] ) = (key -> clientStateVal)
      val newMap = map + keyValue
      map = newMap
      return Success
    } else return Failure

  }

}

object ClientSideStateContainer {
  val theThieveryNumberMap =
    new ClientSideStateContainingMap[TheThieveryNumber] {}

}

object ClientSideModel {}
