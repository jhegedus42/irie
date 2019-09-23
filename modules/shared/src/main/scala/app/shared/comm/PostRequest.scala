package app.shared.comm

import app.shared.comm.PostRequest.{Parameter, Result}

trait PostRequestType
trait ReadRequest extends PostRequestType
trait WriteRequest extends PostRequestType

abstract class PostRequest[RT<:PostRequestType] {
  type ParT <: Parameter
  type ResT <: Result
  type PayLoadT
}

object PostRequest {
  trait Result
  trait Parameter
}





