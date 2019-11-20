package comm.crudRequests

trait JSONConvertable[V] {
  def getJSON(v:      V):      String
  def getObject(json: String): V
}
