package app.shared.comm

import app.shared.comm.PostRequest.{Parameter, Result}

trait PostRequestType
trait ReadRequest extends PostRequestType
trait WriteRequest extends PostRequestType

abstract class PostRequest[+RT <: PostRequestType] {


  // this is covariant, otherwise the Write Request Handler
  // does not compile

  // todo later => "szabvanyositani" az error-handlingot itt
  //   szetszedni request tipusokra :
  //      Read
  //      Write
  //      mindket tipusnak mas az error handling-je
  //

  type ParT <: Parameter
  type ResT <: Result
  type PayLoadT
}

object PostRequest {
  trait Result
  trait Parameter
}
