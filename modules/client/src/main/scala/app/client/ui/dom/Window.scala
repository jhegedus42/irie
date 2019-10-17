package app.client.ui.dom

object Window {
  def setLoggedInUUID(userUUID:String) : Unit={
    // todo-now-2 implement login logic
    import org.scalajs.dom.window
    window.name = s"$userUUID"
  }
  def getLoggedInUUID:String={
    import org.scalajs.dom.window
    window.name
  }

  def runWindowNameTest() : Unit ={

    import org.scalajs.dom.window

    val s: String = window.name

    var i = 0

    try {
      i = Integer.parseInt(s)
    } catch {
      case _ => i = 0
    }

    val i2 = i + 1
    val s2 = s"$i2"

    println(s"old window.dom = ${window.name}")

    window.name = s"$s2"

    println(s"new window.dom = ${window.name}")
  }

}
