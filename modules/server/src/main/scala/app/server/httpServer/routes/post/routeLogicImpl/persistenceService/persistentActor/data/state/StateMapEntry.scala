package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state

import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class StateMapEntry(
    untypedRef:     UntypedRef,
    entityAsString: EntityAsString
) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 =
      entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
  def makeFromEntity[V<:EntityValue[V]](e:Entity[V]):StateMapEntry ={
     val utr:UntypedRef = e.refToEntity
    ???
  }
}
