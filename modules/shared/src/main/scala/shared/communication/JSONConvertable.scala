package shared.communication

trait JSONConvertable[V] {
  def toJSON(v:              V):      String
  def fromJSONToObject(json: String): V
}
