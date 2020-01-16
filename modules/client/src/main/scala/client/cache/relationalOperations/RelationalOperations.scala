package client.cache.relationalOperations

import client.cache.Cache
import client.sodium.core.Cell
import shared.dataStorage.{Ref, TypedReferencedValue, Value}

object RelationalOperations {

  def resolve[V <: Value[V]](
    r: Cell[Ref[V]]
  )(
    implicit
    c: Cache[V]
  ): Cell[TypedReferencedValue[V]] = {

    ???
  }

}
