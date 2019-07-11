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
    ): TypedRefToClientState[CSE] = {
    val ref: TypedRefToClientState[CSE] = TypedRefToClientState.makeNew[CSE]
    val clientStateVal: ClientStateVal[CSE] =
      ClientStateVal( ref, clientStateEntity )
    val keyValue
        : ( TypedRefToClientState[CSE], ClientStateVal[CSE] ) = (ref -> clientStateVal)
    val newMap = map + keyValue
    map = newMap
    ref
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
  def getCSE[TRCS <: TypedRefToClientState[CSE]](
      key: TRCS
    ): Option[ClientStateVal[CSE]] = {
    map.get( key )
  }

}

object ClientSideStateContainer {
  val theThieveryNumberMap =
    new ClientSideStateContainingMap[TheThieveryNumber] {}

}

object ClientSideModel {}
