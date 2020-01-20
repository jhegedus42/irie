package client.ui.compositeWidgets.specific.image

import client.cache.{Cache, CacheMap}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import shared.dataStorage.model.ImageWithQue

object ImageList {

  lazy val listOfImages = {
    SWPreformattedText(
      Cache
        .imgCache
        .cellLoop
        .updates()
        .map(
          (c: CacheMap[ImageWithQue]) =>
            c.getPrettyPrintedString
        )
    )
  }

}
