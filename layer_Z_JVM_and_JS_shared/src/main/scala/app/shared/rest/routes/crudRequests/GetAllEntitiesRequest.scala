//package app.shared.rest.routes.crudRequests
//
//import app.shared.SomeError_Trait
//import app.shared.data.model.Entity.{Data, Entity}
//import app.shared.data.model.LineText
//import app.shared.data.ref.{Ref, RefVal}
//import app.shared.rest.routes.Command
//
//import scala.reflect.ClassTag
//import scalaz.\/
//
//case class GetAllEntitiesRequest[E <: Entity: ClassTag]() extends Command[E] {
//  type Params = Unit
//  type Result = \/[SomeError_Trait, List[RefVal[E]]]
//
//  override def getServerPath = "getAll" + implicitly[ClassTag[E]].runtimeClass.getName
//
//  def queryURL: String = "/" + getServerPath
//}
//
//object GetAllEntitiesRequest {
//  implicit val gAEsLineText = new GetAllEntitiesRequest[LineText]()
//}
