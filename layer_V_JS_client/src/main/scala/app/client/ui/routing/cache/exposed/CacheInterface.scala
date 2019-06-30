package app.client.ui.routing.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import CacheStates.CacheState
import app.client.ui.routing.cache.hidden.EntityCacheMap
import slogging.LazyLogging


object ReRenderTriggererHolderSingletonGloballyAccessibleObject {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer) = {
    triggerer = Some(reRenderTriggerer);
  }

  def triggerReRender() = {

    println("we are now in triggerReRender")

    val t = triggerer

    val ne = (t.nonEmpty)


    println(s"(t.nonEmpty) = $ne")



    if (ne) {

      val tr_naked: ReRenderTriggerer = t.head

      println(tr_naked)

      tr_naked.triggerReRender()

    }
  }

  case class ReRenderTriggerer(triggerReRender: () => Unit)

}

class CacheInterface() extends LazyLogging {

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  private lazy val cacheLineText: EntityCacheMap[LineText] =
    new EntityCacheMap[LineText](this)

  def readLineText(ref: TypedRef[LineText]): CacheState[LineText] = {

    val res: CacheState[LineText] = cacheLineText.readEntity(ref)
    res
  }


  private[cache] def reRenderShouldBeTriggered() = {
    println("so now we try to trigger a re-render in reRenderShouldBeTriggered()")
      ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()

  }

}


