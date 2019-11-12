package comm.crudRequests

import dataStorage.{Ref, Note, ReferencedValue, User, Value}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

sealed trait CRUDReq[V <: Value[V]] {

  def getRouteName[Req <: CRUDReq[V]](
  )(
    implicit
    ct_pl:  ClassTag[V],
    ct_req: ClassTag[Req]
  ): String = {
    val name_req = ct_req.runtimeClass.getSimpleName
    val name_pl  = ct_pl.runtimeClass.getSimpleName
    s"route_${name_req}_${name_pl}_auto_generated"
  }

}
case class GetAllEntityiesForUser[V <: Value[V]](
  par: Ref[User],
  res: Option[ReferencedValue[V]])
    extends CRUDReq[V]
