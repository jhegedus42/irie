//package app.server.persistence.validation
//
//import app.shared.data.model.LineList
//
///**
//  * Created by joco on 28/07/2017.
//  */
//
//trait Constraint[T]
//{
//  def validate(t: T):Boolean
//}
//
//trait Validation[T] {
////  def validateConstraint(c: Constraint[LineList]): Boolean = ???
//  def getConstraints:List[Constraint[T]]
//}
//
//object Validation{
//
//  implicit object LineListValidation extends Validation[LineList]{
//
//    override def getConstraints: List[Constraint[LineList]] = List(OneLineListPerQue)
//
//    object OneLineListPerQue extends Constraint[LineList]{
////      override def validate(t: LineList): Boolean = ???
//      // 4588124cf0dc419dbc3fefb5593e3d48
//      // check that a given que cannot be in more than one LineList
//      // let's compute Que->Set[LineList] LineWithQue-s
//      // Que->Set(LineWithQue)  // constraint - csak 0 v 1 lehet
//      // LineWithQue->
//
//    }
//
//  }
//}


