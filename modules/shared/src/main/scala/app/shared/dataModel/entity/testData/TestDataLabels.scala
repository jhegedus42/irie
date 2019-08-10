package app.shared.dataModel.entity.testData

import io.circe.Json

object TestDataLabels {
  import io.circe.generic.auto._
  import io.circe.syntax._

  def toJSON( l: TestDataLabel ): Json = l.asJson
}

sealed trait TestDataLabel
case object LabelOne extends TestDataLabel
case object LabelTwo extends TestDataLabel
case object LabelThree extends TestDataLabel
case object TestDataForUsers extends TestDataLabel
