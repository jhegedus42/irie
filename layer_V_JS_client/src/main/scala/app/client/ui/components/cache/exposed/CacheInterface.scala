package app.client.ui.components.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.Ref
import CacheStates.CacheState
import app.client.ui.components.cache.hidden.EntityCacheMap
import app.client.ui.components.rootComponents.cacheTestRootComp.ReRenderTriggererContainer.ReRenderTriggerer
import slogging.LazyLogging

/**
  * indirection , not allowing direct access to EntityCacheMap
  *
  */

object CacheInterface extends LazyLogging {

  private var nrOfRerendTriggererSets = 0
  private lazy val cacheLineText: EntityCacheMap[LineText]  = new EntityCacheMap[LineText]
  private[this] var reRenderTriggerer:          Option[ReRenderTriggerer] = None

  private[cache] def reRenderShouldBeTriggered() = {

    reRenderTriggerer.foreach( _.triggerReRender() )
  }


  def setReRenderTriggerer(triggerer: ReRenderTriggerer ) = {
    println( "RERENDER - CacheInterface rerender is called." )
    println(
      s"Number of times the reRenderTriggerer has \n" +
      s"been updated so far $nrOfRerendTriggererSets"
    )
    println(
      s" this is due to mounting a component, \n" +
      s" which uses the Cache to get data from the server"
    )
    this.logger.trace( "We set the RE-RENDER triggerer because the page was mounted" )
    nrOfRerendTriggererSets = nrOfRerendTriggererSets + 1
    this.reRenderTriggerer  = Some( triggerer )
  }

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  /**
    *
    *
    // erre lehetni irni type class-okat: vmi altalanos getEntity
    // metodust, azaz pl. attol fuggoen h. milyen entity't ker a react comp mas instance hivodik meg...
    // de ezt majd irjuk meg azutan ha a konkret dolgok kesz vannak
    // cache will have a separate map for each entity
    // for each view
    // ezek adnak egy type safety-t, nem kell kasztolgatni, az is latszik tisztan, hogy milyen
    // entity-t vannak hasznalatban
    // es nem is kell readLineText, stb ...
    * @param ref
    * @return
    */
  def readLineText(ref: Ref[LineText] ): CacheState[LineText] = {
    this.logger.trace(
      s"BEFORE calling readEntity($ref) on cacheLineText\n",
      s"Cache was asked for $ref \n" +
//        s"The state of the cache is ${cacheLineText.map}\n" +
        s"The state of the cache pretty printed is : \n" +
        s"${cacheLineText.getCacheContentAsPrettyString}\n"
    )

    val res: CacheState[LineText] = cacheLineText.readEntity( ref )

    this.logger.trace(
      s"AFTER calling readEntity($ref) on cacheLineText\n",
      s"Cache was asked for $ref \n" +
//        s"The state of the cache is ${cacheLineText.map}\n" +
        s"The state of the cache pretty printed is : \n" +
        s"${cacheLineText.getCacheContentAsPrettyString}\n"
    )

    this.logger.trace( s"result of the readEntity call to the cache is: \n $res" )
    res
  }

}


