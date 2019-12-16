package shared.crudRequests

trait CanProvideRouteName[V] {
  def getRouteName: String
}
