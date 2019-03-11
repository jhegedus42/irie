package app.server.persistence

import app.shared.data.model.TypeAsString
import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.ref.{Ref, RefDyn, RefVal, RefValDyn}
import app.shared.{EntityDoesNotExistError, EntityIsNotUpdateableError, InvalidVersionError, SomeError_Trait, StateOpsError, TypeError}
import scalaz.{-\/, Disjunction, \/, \/-}

import scala.reflect.ClassTag

/**
  * Ez mire jó?
  * Ez tárolja az app állapotát. Ez a state.
  *
  *
  * Ki használja ?
  *   - Az ImPersistentActor használja.
  *     - És miért ?
  *       - Hogy tárolja, az applikáció állapotát valamiben, amin vannak segéd metódusok,
  *         amikkel updatelni tudja az app állapotát "kényelmesen".
  *           - Például
  *             - insert Entity
  *             - update Entity
  *             - does Entity exist ?
  *             - get Entities of Given Type [Type]
  *
  * Ez immutable.
  *
  * @param stateMap
  */
case class ApplicationState(stateMap: Map[RefDyn, RefValDyn] = Map.empty ) {

  // Random UUID: da1d5a2c10374c358987fd30643b8812
  // commit fc5bb550a0436ada8876f7c8a18d4b4bf9407091
  // Date: Thu Aug  2 07:52:01 EEST 2018

  case class StateUpdateError(s: String )

  def insertEntity(refValDyn: RefValDyn ): \/[SomeError_Trait, ApplicationState] = {
    \/-( this.copy( stateMap = this.stateMap + (refValDyn.r -> refValDyn) ) )
  }

  def updateEntity(refValDyn: RefValDyn ): \/[SomeError_Trait, ( ApplicationState, RefValDyn )] = {
    val rr: RefDyn = refValDyn.r

    if (!stateMap.contains( rr ))
      return -\/(
        EntityIsNotUpdateableError(
          "entity does not exist",
          Some( EntityDoesNotExistError( "while updating State" ) )
        )
      )

    if (refValDyn.version == stateMap( refValDyn.r ).version) {
      val newVal = refValDyn.copy( version = refValDyn.version.inc() )
      return \/-( this.copy( stateMap = this.stateMap + (refValDyn.r -> newVal) ), newVal )
    } else {
      val r = -\/(
        InvalidVersionError( "while updating the state", stateMap( refValDyn.r ).version,
                             refValDyn.version )
      )
      return r
    }

  }

  def doesEntityExist(e: Entity ): Boolean =
    stateMap.values.map( rvd => rvd.e ).toSet.contains( e )

  def getEntitiesOfGivenType[E <: Entity: ClassTag](): \/[SomeError_Trait, List[RefVal[E]]] = {
    val et: TypeAsString = TypeAsString.make[E]
    val r: List[Disjunction[TypeError, RefVal[E]]] =
      stateMap.values.filter( rvd => rvd.r.et == et ).map( _.toRefVal[E] ).toList

    if (r.forall( dj => dj.isRight ))(\/-( r.map( _.toEither.right.get ) ) )
    else -\/( StateOpsError( "getEntities type error - this should not happen" ) )
  }

  private[server] def getEntity[E <: Entity: ClassTag](r: Ref[E] ): \/[SomeError_Trait, RefVal[E]] = {

    if (!r.isTypeCorrect) return -\/( TypeError( "State.getEntity - 1" ) )
    else {
      val rd: RefDyn = r
      getEntityDyn( rd ).flatMap( _.toRefVal[E] )
      // checks that the dyn type from the map matches with the expected type E
    }
  }

  import scalaz._
  import Scalaz._

  private def getEntityDyn(rd: RefDyn ): \/[SomeError_Trait, RefValDyn] = {
    val r: Option[RefValDyn] = this.stateMap.get( rd )
    val r2 =
      r.toRightDisjunction( EntityDoesNotExistError( s"StateOps.getEntity " + rd ) )
    r2 match {
      case -\/( a ) => -\/( a )
      case \/-( b ) => {
        if (b.r.et == rd.et) \/-( b )
        else -\/( (TypeError( "State.getEntity - 2" ) ) )
      }
    }

  }

}
