package app.server.persistence.validation

import app.shared.data.model.UserLineList

/**
  * Created by joco on 28/07/2017.
  */

trait Constraint[T]
{
  def validate(t: T):Boolean
}

trait Validation[T] {
  def validateConstraint(c: Constraint[UserLineList]): Boolean = ???
  def getConstraints:List[Constraint[T]]
}

object Validation{

  implicit object LineListValidation extends Validation[UserLineList]{

    override def getConstraints: List[Constraint[UserLineList]] = List(OneLineListPerQue)

    object OneLineListPerQue extends Constraint[UserLineList]{
      override def validate(t: UserLineList): Boolean = ???
      // 4588124cf0dc419dbc3fefb5593e3d48
      // check that a given que cannot be in more than one LineList
      // let's compute Que->Set[LineList] LineWithQue-s
      // Que->Set(LineWithQue)  // constraint - csak 0 v 1 lehet
      // LineWithQue->

    }

  }
}


