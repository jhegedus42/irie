package shared.crudRESTCallCommands

trait CanProvideRouteName[V] {
  def getRouteName: String
}
