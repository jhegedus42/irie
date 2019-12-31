package client.ui.compositeWidgets.image

import client.cache.{Cache, CacheMap}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import shared.dataStorage.{Image }

object ImageList {

  lazy val listOfImages = {
    SWPreformattedText(
      Cache
        .imgCache
        .cellLoop
        .updates()
        .map(
          (c: CacheMap[Image]) =>
            c.getPrettyPrintedString
        )
    )
  }

}
