package app.server.stateAccess.tests

import akka.http.scaladsl.testkit.ScalatestRouteTest
import app.server.persistence.ApplicationState
import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
import app.testHelpersShared.data.TestEntities
import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.ref.UUID_Utils.UUID
import app.shared.data.ref.{TypedRefVal, TypedRef, Version}
import app.shared.data.model.{Note, TypeAsString}
import app.shared.{
  EntityDoesNotExistError,
  EntityIsNotUpdateableError,
  InvalidVersionError,
  SomeError_Trait,
  TypeError
}
import app.testHelpersServer.state.TestData
import org.scalatest.{Assertion, Matchers, WordSpec, mock}

import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag
import scalaz.{-\/, \/, \/-}

trait EntityServiceTest_BaseTrait
    extends WordSpec with Matchers with ScalatestRouteTest {

  def getEntityService(s: ApplicationState ): InterfaceToStateAccessor

  import scala.concurrent.duration._

  def isEntityPresentByGetEntity[E <: Entity: ClassTag](
      typedInterfaceToPersistenceActor: InterfaceToStateAccessor,
      refVal_Result:                    TypedRefVal[E]
    ): Boolean = {
    val res: Future[\/[SomeError_Trait, TypedRefVal[E]]] =
      typedInterfaceToPersistenceActor.getEntity( refVal_Result.r )

    val exp: \/-[TypedRefVal[E]] = \/-( refVal_Result )
    val aw:  \/[SomeError_Trait, TypedRefVal[E]] = Await.result( res, 2 seconds )
    (aw == exp) && (aw.isRight)
  }

  private[this] def newRefValLine(uuid: UUID ): TypedRefVal[Note] = {
    val r = TypedRef.makeWithUUID[Note]( uuid = uuid )
    val line:   Note = Note( content = "text" )
    val refVal: TypedRefVal[Note] = TypedRefVal( r, line, Version() )
    return refVal
  }

  "getEntityByRef " should {

    "return an EntityResolverError if Entity cannot be resolved because " +
      "the type of the passed Ref is not consistent with the type parameter " +
      "of getEntityRef[E]" in {
      val mock =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )
      val r: TypedRef[Note] =
        TypedRef[Note]( dataType = TypeAsString( "kamu" ) )
      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.getEntity[Note]( r )
      val ar: \/[SomeError_Trait, TypedRefVal[Note]] =
        Await.result( res, 2 seconds )
      assert( ar.isLeft )
      mock.shutDownService()
    }

    "return RefVal[Line]  - if uuid and type is right" in {
      val mockEs: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )

      isEntityPresentByGetEntity[Note](
        mockEs,
        newRefValLine( TestEntities.theUUIDofTheLine )
      )
      mockEs.shutDownService()
    }

    "complain  - if entity is not present" in {
      val mockEs: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )

      assert(
        !isEntityPresentByGetEntity[Note](
          mockEs,
          newRefValLine( TestEntities.theUUIDofTheLine_incorrect )
        )
      )
      mockEs.shutDownService()
      // the problem with this is that it is very imperative ...
      // easy to forget to put this here ...
      //   but i dont know any better solution
      //   or tracking who created the resource and who needs to shut it down ...
      //   similar to reference counting
    }
  }

  "updateEntityByRefVal " should {

    "typeError " in {

      val mockES: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )
      val line: Note = Note( content = "test" )

      val r: TypedRef[Note] =
        TypedRef[Note]( dataType = TypeAsString( "kamu" ) )
      val refVal_updateToThis
          : TypedRefVal[Note] = TypedRefVal( r, line, Version() ) // tetszoleges verziora ennek fail-elnie kell

      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mockES.updateEntity[Note]( refVal_updateToThis )

      val ar: \/[SomeError_Trait, TypedRefVal[Note]] =
        Await.result( res, 200 seconds )
      println( ar.toEither.left.get )

      val ass = ar.toEither.left.get shouldBe a[TypeError]
      print( ass )
      mockES.shutDownService()

    }

    "complain if the to-be-updated entity does not exist -1" in {
      val mock = getEntityService( ApplicationState() )
      val r = TypedRef.makeWithUUID[Note](
        uuid = UUID( TestEntities.theUUIDofTheLine )
      )

      val line: Note = Note( content = "test" )

      val refVal: TypedRefVal[Note] = TypedRefVal( r, line, Version() )

      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.updateEntity[Note]( refVal )

      val ar = Await.result( res, 20 seconds )

      assert( ar.isLeft )
      println( res )

      val r2: Assertion = ar.toEither.left.get shouldBe a[
        EntityIsNotUpdateableError
      ]
      ar.toEither.left.get shouldBe a[EntityIsNotUpdateableError]
      val ass = ar.toEither.left.get
        .asInstanceOf[EntityIsNotUpdateableError]
        .edne
        .get shouldBe a[EntityDoesNotExistError]
      println( r2 )
      mock.shutDownService()
    }

    "complain if entity does not exist (UUID is incorrect)" in {
      val mock: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )
      val r = TypedRef.makeWithUUID[Note](
        uuid = UUID( TestEntities.theUUIDofTheLine_incorrect )
      )

      val line: Note = Note( content = "test" )

      val refVal: TypedRefVal[Note] = TypedRefVal( r, line, Version() )

      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.updateEntity[Note]( refVal )

      val ar = Await.result( res, 2 seconds )
      assert( ar.isLeft )
      println( res )
      val ass = ar.toEither.left.get
        .asInstanceOf[EntityIsNotUpdateableError]
        .edne
        .get shouldBe a[EntityDoesNotExistError]
//      ar.toEither.left.get. shouldBe a[EntityDoesNotExistError]
      mock.shutDownService()
    }

    "complain if version is newer - client wants to update entity with newer " +
      "version than the version which is currently in the DB - this should come from some bug or hacking" in { // how can this even be ???
      val mock: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )
      val r = TypedRef.makeWithUUID[Note](
        uuid = UUID( TestEntities.theUUIDofTheLine )
      )

      val line: Note = Note( content = "test" )

      val refVal: TypedRefVal[Note] = TypedRefVal( r, line, Version().inc() )

      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.updateEntity[Note]( refVal )

      val ar = Await.result( res, 2 seconds )
      assert( ar.isLeft )
      println( res )
      val ass = ar.toEither.left.get shouldBe a[InvalidVersionError]
      //      ar.toEither.left.get. shouldBe a[EntityDoesNotExistError]
      assert(
        ar == -\/(
          InvalidVersionError( "while updating the state",
                              expected = Version( 0 ),
                              actual = Version( 1 ) )
        )
      )
      mock.shutDownService()
    }

    "complain if version is too old" in { // how can this even be ???
      val mock: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelTwo_OneLine_WithVersionOne_nothing_else
        )
      val r = TypedRef.makeWithUUID[Note](
        uuid = UUID( TestEntities.theUUIDofTheLine )
      )

      val line: Note = Note( content = "test" )

      val refVal: TypedRefVal[Note] = TypedRefVal( r, line, Version() )

      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.updateEntity[Note]( refVal )

      val ar = Await.result( res, 2 seconds )
      assert( ar.isLeft )
      println( res )
      val ass = ar.toEither.left.get shouldBe a[InvalidVersionError]
      //      ar.toEither.left.get. shouldBe a[EntityDoesNotExistError]
      assert(
        ar == -\/(
          InvalidVersionError( "while updating the state",
                              expected = Version( 1 ),
                              actual = Version( 0 ) )
        )
      )
      mock.shutDownService()
    }

    "return update RefVal[Line]  - if uuid ,type and version are right" in { // --most itt vagyunk
      val mock: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )
      testUpdate( TestEntities.refValOfLineV0, mock )
      mock.shutDownService()
    }

  }

  def testUpdate(l: TypedRefVal[Note], mock: InterfaceToStateAccessor ) = {

    val originalTitle_v0: TypedRefVal[Note] = l
    val notExisting: TypedRefVal[Note] = newRefValLine(
      TestEntities.theUUIDofTheLine_incorrect
    )

    import monocle.macros.syntax.lens._

    val updatedTitle_v0: TypedRefVal[Note] =
      originalTitle_v0.lens( _.v.content ).set( "helloTEXTNEW" )
    //atirjuk a title-t hello-ra
    val updatedTitle_v1: TypedRefVal[Note] = updatedTitle_v0
      .lens( _.version )
      .modify( _.inc() ) //megnoveljuk a verzioszamot

    //elotte

    assert( isEntityPresentByGetEntity[Note]( mock, originalTitle_v0 ) ) //jelen van

    assert( !isEntityPresentByGetEntity[Note]( mock, notExisting ) ) // nincs jelen

    assert( !isEntityPresentByGetEntity[Note]( mock, updatedTitle_v0 ) ) // nincs jelen

    assert( !isEntityPresentByGetEntity[Note]( mock, updatedTitle_v1 ) ) // nincs jelen

    val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
      mock.updateEntity( updatedTitle_v0 )

    // utana
    val expected = \/-( updatedTitle_v1 )
    val aw = Await.result( res, 2 seconds )

    assert( aw == expected )
    assert( aw.isRight )

    assert( !isEntityPresentByGetEntity[Note]( mock, originalTitle_v0 ) ) // nincs jelen

    assert( !isEntityPresentByGetEntity[Note]( mock, notExisting ) ) // nincs jelen

    assert( !isEntityPresentByGetEntity[Note]( mock, updatedTitle_v0 ) ) // nincs jelen

    assert( isEntityPresentByGetEntity[Note]( mock, updatedTitle_v1 ) ) // jelen van
  }

  "createEntity " should {
    "create a Line if parameters are ok" in {
      val mock: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )

      // csinalunk itt vmi line-t

      val line = Note( content = "test" )
      // assert ilyen line nincsen
      val r1 = Await.result( mock.doesEntityExist( line ), 2 seconds )
      assert( !r1 )
      val res: Future[\/[SomeError_Trait, TypedRefVal[Note]]] =
        mock.createEntity( line )
      val aw: \/[SomeError_Trait, TypedRefVal[Note]] =
        Await.result( res, 2 seconds )
      assert( aw.isRight )
      val rw: TypedRefVal[Note] = aw.toEither.right.get
      val r2 = Await.result( mock.doesEntityExist( line ), 2 seconds )
      assert( r2 )
      testUpdate( rw, mock )
      mock.shutDownService()

    }
  }

  "getEntities" should {
    "return one Line" in {
      val mockEs: InterfaceToStateAccessor =
        getEntityService(
          TestData.TestState_LabelOne_OneLine_WithVersionZero_nothing_else
        )

      val r: \/[SomeError_Trait, List[TypedRefVal[Note]]] =
        Await.result( mockEs.getAllEntitiesOfGivenType[Note], 2 seconds )
//      r shouldBe \/-
      r.toEither.right.get shouldBe List(
        TestData.LabelOneEntities.lineInState
      )
      r.toEither.right.get should not be List()

      mockEs.shutDownService()

      //a805e8a744af4e74b2e5d49e68a6e6dc

    }

  }
}
