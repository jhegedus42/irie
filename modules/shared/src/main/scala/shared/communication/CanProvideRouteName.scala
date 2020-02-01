package shared.communication

trait CanProvideRouteName[V] {
  def getRouteName: String
}
