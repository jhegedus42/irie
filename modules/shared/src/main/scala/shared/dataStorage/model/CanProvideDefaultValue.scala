package shared.dataStorage.model

trait CanProvideDefaultValue[E] {
  def getDefaultValue:E

}

object CanProvideDefaultValue {
  def defValOf[E:CanProvideDefaultValue]={
    implicitly[CanProvideDefaultValue[E]].getDefaultValue
  }
}

