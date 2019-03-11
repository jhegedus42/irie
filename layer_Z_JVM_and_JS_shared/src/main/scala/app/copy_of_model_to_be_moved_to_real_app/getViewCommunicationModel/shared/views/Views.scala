package app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views

abstract class View {
  type Par <: Parameter
  type Res <: Result
}

trait Result
trait Parameter // marker trait parameter nelkul egy nagy rakas szar

//trait Request[Response] {
//}

//case class PersonListResponse(persons: List[Person])

//case class GetPersonListRequest(searchText: String) extends Request[PersonListResponse]



object View1_HolderObject {

  case class View1_Par(s: String ) extends Parameter // View1-t parameterkent ide a Parameter[...-be]

  case class View1_Res(res: String ) extends Result

  class View1 extends View {
    type Par = View1_Par
    type Res = View1_Res
  }
}

object View2_HolderObject {

  case class View2_Par(i: Int) extends Parameter

  case class View2_Res(res: Int) extends Result

  class View2 extends View {
    type Par = View2_Par
    type Res = View2_Res
  }
}
