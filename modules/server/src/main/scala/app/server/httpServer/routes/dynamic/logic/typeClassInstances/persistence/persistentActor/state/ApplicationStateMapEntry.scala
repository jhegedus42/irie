package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state

import app.shared.entity.asString.EntityAsString



private[persistence] case class ApplicationStateMapEntry(
    untypedRef:    UntypedRef,
    entityAsString:EntityAsString
){
  def asSimpleString():String ={
    val s1=untypedRef.asSimpleString()
    val s2=entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}

