package client.ui.compositeWidgets.specific.image.svg

import io.circe.{Decoder, Json}
import shared.dataStorage.model.VisualHint

object VisualHintDemoData {

  lazy val benchDemoHint = {
    s"""
       |
       |{
       |    "title" : "default image title",
       |    "fileData" : {
       |        "fileName" : {
       |            "fileNameAsString" : "ca8b6452-02d0-425b-89f0-6cbcc4b475cb.jpeg"
       |        },
       |        "sizeInPixel" : {
       |            "width" : 427,
       |            "height" : 640
       |        }
       |    },
       |    "hintToThisImage" : {
       |        "rect" : {
       |            "upperLeftCornerXY" : {
       |                "x" : 168,
       |                "y" : 198
       |            },
       |            "size" : {
       |                "width" : 198,
       |                "height" : 226
       |            }
       |        }
       |    },
       |    "placeForHintToNextImage" : {
       |        "rect" : {
       |            "upperLeftCornerXY" : {
       |                "x" : 50,
       |                "y" : 50
       |            },
       |            "size" : {
       |                "width" : 20,
       |                "height" : 20
       |            }
       |        }
       |    }
       |}
       |
       |""".stripMargin

  }

  lazy val benchDemoHintDecoded: VisualHint = {
    jsonVisualHintParser(benchDemoHint)
  }

  def jsonVisualHintParser(
                            visualHintAsJSON: String
                          )(
                            implicit
                            vh: Decoder[VisualHint]
                          ) = {
    import io.circe.generic.JsonCodec
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._

    lazy val s = decode(visualHintAsJSON)
    if(s.toTry.toOption.isDefined) println("json is defined")
      else println("json is not defined")
    s.toTry.toOption.head
  }

}
