package app.server.stateAccess.generalQueries

import app.server.persistence.ApplicationState
import app.server.persistence.persActor.Commands.GetStatePAResponse
import app.shared.SomeError_Trait
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{Ref, RefVal, RefValDyn}
import io.circe.Decoder.state

import scala.concurrent.Future
import scala.reflect.ClassTag
import scalaz.\/

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by joco on 17/10/2017.
  */
trait StateAccessorBase extends InterfaceToStateAccessor {
  self: HasPersistentActor =>

  //7ab5e22e4b0641a081acdfda82f72390 commit 04a4ce0194060a090583a831d19bea4ba9408ff1 Thu Oct 19 21:57:39 EEST 2017

  override def doesEntityExist[E <: Entity: ClassManifest](e: E ): Future[Boolean] = {
    actor.getState.map( _.state.doesEntityExist( e ) )
  }

  override def createEntity[E <: Entity: ClassTag](e: E ): Future[\/[SomeError_Trait, RefVal[E]]] = {
    for {
      r: \/[SomeError_Trait, RefValDyn] <- actor.createEntity( e ).map( _.payload )

      z: \/[SomeError_Trait, RefVal[E]] = for {
        r1 <- r
        rv <- r1.toRefVal[E]
      } yield (rv)

    } yield (z)
  }

  def updateEntity[E <: Entity: ClassTag](rv: RefVal[E] ): Future[\/[SomeError_Trait, RefVal[E]]] = {
    actor
      .updateEntity( rv ).map( uer => {
        for {
          _ <- rv.r.isTypeAndUUIDCorrect2 // checks if the ref is ok that we got from the actor
          x <- (uer.payload.flatMap( _.toRefVal[E] ) )
        } yield (x)
      } )

  }

  def getEntity[E <: Entity: ClassTag](r: Ref[E] ): Future[\/[SomeError_Trait, RefVal[E]]] = {
    //hash 714b03f2a4fe4fd1a27b12f805e5bc56
    def f(x:GetStatePAResponse): \/[SomeError_Trait, RefVal[E]] = {
      val s=x.state
      assert(s!=null,{println ("state is null while getting entity")})
      s.getEntity( r )
    }

    actor.getState.map( f )
  }

  override def getAllEntitiesOfGivenType[
      E <: Entity: ClassTag
    ]: Future[\/[SomeError_Trait, List[RefVal[E]]]] = {
    println( "we get entities - in state accessor" )
    val s: Future[GetStatePAResponse] = actor.getState
    s.map( _.state.getEntitiesOfGivenType() )

    //111037318c7c4c808aaea0eade1a6052
  }

}
