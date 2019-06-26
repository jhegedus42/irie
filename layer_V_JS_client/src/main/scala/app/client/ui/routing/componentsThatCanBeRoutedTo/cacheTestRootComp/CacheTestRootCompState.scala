package app.client.ui.routing.componentsThatCanBeRoutedTo.cacheTestRootComp

import app.shared.data.model.LineText
import app.shared.data.ref.RefVal

// a root comp -nek van allapota ?
case class CacheTestRootCompState(i: Int, lineTextOption: Option[RefVal[LineText]] )
