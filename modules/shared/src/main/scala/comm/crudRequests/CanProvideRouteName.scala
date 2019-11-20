package comm.crudRequests

trait CanProvideRouteName[V] {
  def getRouteName: String
}
