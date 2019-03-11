package app.testHelpersShared.data

import io.circe.Json

object TestDataLabels{
  import io.circe.generic.auto._
  import io.circe.syntax._

 sealed trait TestDataLabel
case object LabelOne extends TestDataLabel
case object LabelTwo extends TestDataLabel
case object LabelThree extends TestDataLabel

  def toJSON(l:TestDataLabel): Json = l.asJson
}



//ad9392201d604e93b0c0e7f976f3f202
