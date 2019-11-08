package app.shared.comm.sodium

import app.shared.entity.entityValue.EntityType

sealed trait SodiumCRUDReq[V <: EntityType[V]]{
  type Par
  type Resp
}

trait SodiumParamConverterImpl[Req<:SodiumCRUDReq[V],V<:EntityType[V]]{
  implicit def parToString(par:Req#Par):String
  implicit def respToString(par:Req#Resp):String
  implicit def stringToResp(s:String):Req#Resp
  implicit def stringToPar(s:String):Req#Par
}