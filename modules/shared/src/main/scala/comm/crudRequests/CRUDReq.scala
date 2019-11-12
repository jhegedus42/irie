package comm.crudRequests

import dataStorage.stateHolder.UserMap
import dataStorage.{Note, Ref, ReferencedValue, User, Value}
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

  def getPersActorCommand:Commands.Command

}

@JsonCodec
case class GetAllEntityiesForUser[V <: Value[V]](
  par: Ref[User],
  res: UserMap)
    extends CRUDReq[V] {
  override def getPersActorCommand: Commands.Command = ???
}



object Commands{
  sealed trait Command
  case object ShutDown extends Command
  case class GetUsersEntities(uuid:String) extends Command
}
