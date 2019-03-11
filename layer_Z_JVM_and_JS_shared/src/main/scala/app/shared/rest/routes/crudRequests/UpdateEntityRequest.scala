//package app.shared.rest.routes.crudRequests
//
///**
//  * Created by joco on 17/12/2017.
//  */
//
//
//import app.shared.SomeError_Trait
//import app.shared.data.model.Entity.{Data, Entity}
//import app.shared.data.ref.RefVal
//import app.shared.rest.routes.Command
//
//import scala.reflect.ClassTag
//import scalaz.\/
//
///**
//  * Created by joco on 14/12/2017.
//  *
//  * This class represents/describes a http request that
//  * updates an Entity.
//  *
//  */
//
//
//case class UpdateEntityRequest[E<:Entity:ClassTag]() extends Command[E] {
//  //  type E <:Entity
//  type Params = RefVal[E] //uuid
//  type Result = \/[SomeError_Trait,RefVal[E]]
//
//  override def getServerPath = "update" + implicitly[ClassTag[E]].runtimeClass.getName
//  // server path does not start with /
//
//  def queryURL()= "/" + getServerPath
//}
//
//object UpdateEntityRequest{
//  type UpdateEntityRequestResult[E<:Entity] = UpdateEntityRequest[E]#Result
//  type UpdateEntityRequestParameters[E<:Entity] = UpdateEntityRequest[E]#Params
//}
