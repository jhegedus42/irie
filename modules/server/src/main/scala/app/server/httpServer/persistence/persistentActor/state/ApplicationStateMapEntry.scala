package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.asString.EntityAsString



case class ApplicationStateMapEntry(
    untypedRef:    UntypedRef,
    entityAsString:EntityAsString
){
  def asSimpleString():String ={
    val s1=untypedRef.asSimpleString()
    val s2=entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}

