package client.ui.compositeWidgets.specific.image.svg

import io.circe.{Decoder, Json}
import shared.dataStorage.model.VisualHint

object VisualHintDemoData {

  lazy val jsonVisualHint2 = {
    s"""
       |{
       |    "title" : "csaj padon",
       |    "fileName" : {
       |        "fileNameAsString" : "./befe7bd2-05be-485c-abb0-aaceb88cbc31.jpeg"
       |    },
       |    "hintToThisImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 151,
       |                "y" : 221
       |            },
       |            "size" : {
       |                "width" : 209,
       |                "height" : 217
       |            }
       |        }
       |    },
       |    "placeForHintToNextImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 28,
       |                "y" : 508
       |            },
       |            "size" : {
       |                "width" : 167,
       |                "height" : 125
       |            }
       |        }
       |    }
       |}
       |""".stripMargin
  }

  lazy val jsonVisualHint =
    s"""
       |{
       |    "title" : "default image title",
       |    "fileName" : {
       |        "fileNameAsString" : "./43ea782f-6960-44c4-a0d3-5227917abef7.jpeg"
       |    },
       |    "hintToThisImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 453,
       |                "y" : 159
       |            },
       |            "size" : {
       |                "width" : 422,
       |                "height" : 302
       |            }
       |        }
       |    },
       |    "placeForHintToNextImage" : {
       |        "rect" : {
       |            "center" : {
       |                "x" : 769.6954819088226,
       |                "y" : 114.14639190479977
       |            },
       |            "size" : {
       |                "width" : 231.87857971712867,
       |                "height" : 228.04066253120334
       |            }
       |        }
       |    }
       |}
       |""".stripMargin

  lazy val visualHint1: VisualHint = {
    jsonVisualHintParser(jsonVisualHint)
  }

  lazy val visualHint2: VisualHint = {
    jsonVisualHintParser(jsonVisualHint2)
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
