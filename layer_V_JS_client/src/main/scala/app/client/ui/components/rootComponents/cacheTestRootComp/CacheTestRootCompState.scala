package app.client.ui.components.rootComponents.cacheTestRootComp

import app.shared.data.model.LineText
import app.shared.data.ref.RefVal

case class CacheTestRootCompState(i: Int, lineTextOption: Option[RefVal[LineText]] )
