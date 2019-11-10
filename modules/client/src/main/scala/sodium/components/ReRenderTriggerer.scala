package sodium.components

case class ReRenderTriggerer(f: Option[() => Unit]) {

  def trigger(): Unit = {
    if (f.isDefined) {
      val r: () => Unit = f.get
      r()
    }
  }
}