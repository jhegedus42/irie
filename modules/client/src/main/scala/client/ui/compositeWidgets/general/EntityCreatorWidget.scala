package client.ui.compositeWidgets.general

import client.cache.Cache
import client.ui.atomicWidgets.input.SButton
import shared.dataStorage.{Note, TypedReferencedValue, Value}
import shared.testingData.TestDataStore

case class EntityCreatorWidget[V <: Value[V]](
  creator:    () => V,
  entityName: String)(implicit cache:Cache[V]){

  lazy val createNewEntityButton = SButton(
    s"Create New $entityName",
    Some(() => {

      val newVal =
        TypedReferencedValue[V]( creator() )

      cache.insertEntityStream.send(newVal)
    })
  )

}
