package client.sodium.app.actions

import client.sodium.core._

case class SActionWriteToConsole(s: Stream[String]) {
  s.listen(t => println(t))
}
