package app.shared.dataModel.views

import app.shared.comm.requests.Request
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{Entity, RefToEntity}
import app.shared.dataModel.views.GetEntityRequest.{GetEntityRequestParameter, GetEntityRequestResult}
import app.shared.dataModel.views.SumIntPostRequest.{SumIntView_Par, SumIntView_Res}

object SumIntPostRequest {

    case class SumIntView_Par(x:Int, y:Int ) extends Request.PostReqParameter

    case class SumIntView_Res(sum: Int ) extends Request.PostReqResult



}


class SumIntPostRequest extends Request {
  type Par = SumIntView_Par
  type Res = SumIntView_Res
}

object GetEntityRequest {

  case class GetEntityRequestParameter[V<:EntityValue[V]](par:RefToEntity[V]) extends Request.PostReqParameter

  case class GetEntityRequestResult[V<:EntityValue[V]](res:Option[Entity[V]] ) extends Request.PostReqResult

}

class GetEntityRequest[V<:EntityValue[V]] extends Request {
  type Par = GetEntityRequestParameter[V]
  type Res = GetEntityRequestResult[V]
}


