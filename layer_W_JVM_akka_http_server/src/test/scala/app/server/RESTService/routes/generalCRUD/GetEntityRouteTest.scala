//package app.server.RESTService.routes.generalCRUD
//
//import app.server.RESTService.routes.RoutesTestBase
//import app.shared.data.model.LineText
//import app.shared.data.ref.Ref
//import app.shared.rest.routes.crudRequests.GetEntityRequest
//import app.shared.{EntityDoesNotExistError, InvalidUUIDinURLError}
//import app.testHelpersShared.data.TestEntities
//
///**
//  * Created by joco on 14/12/2017.
//  */
//trait GetEntityRouteTest {
//  this: RoutesTestBase =>
//
//
//
//  "getRoute" should {
//
//    //    "Akka Mock Server (getRouteJSON) " should {
//    // with space at the end, this line does not work.... wtf ? BUG in scalatest ? shit....
//
////    import GetEntityRouteServerSide.Res
//
//    "happy path - return RefVal[Line]  - if the URL and uuid and type is right" in {
//      val refVal = TestEntities.refValOfLineV0
//
////      val params: GetEntityQueryParameters = GetEntityQueryParameters( refVal.r.uuid )
//
////      val url: String =
////        ClientSideGetEntityURL( entityType = EntityType.make[LineText], params ).getURLWithHostAsString
//
//      val url: String = GetEntityRequest[LineText]().queryURL(refVal.r)
//
//      testGetEntityHelper(url, assert = r => {r.toEither.right.get shouldBe refVal })
//    }
//
//    "EntityDoesNotExistError - if uuid is correct but no Line exists with that UUID wrong" in {
//      val ref: Ref[LineText] = Ref.make[LineText]()
//      // random uuid
//
////      val params: GetEntityQueryParameters = GetEntityQueryParameters( ref.uuid )
////      val url:    String                   = ClientSideGetEntityURL( entityType = ref.entityType, params ).getURLWithHostAsString
//
//      val url: String = GetEntityRequest[LineText]().queryURL(ref)
//
//      testGetEntityHelper(url, r => {r.toEither.left.get shouldBe a[EntityDoesNotExistError] })
//    }
//
//    "InvalidURLError - if UUID in URL is incorrect" in {
//      // what does it mean that URL is shitty => if the UUID is shitty, incorrectly formatted
//      // get a Ref with bad uuid
//
//      import monocle.macros.syntax.lens._
//      val ref: Ref[LineText] =
//        TestEntities.refToLine.lens( _.uuid.id ).set( "IandI" )
//      //fuking up the uuid - on purpose
//
////      val params: GetEntityQueryParameters = GetEntityQueryParameters( ref.uuid )
////      val url:    String                   = ClientSideGetEntityURL( entityType = ref.entityType, params ).getURLWithHostAsString
//
////      val url : String = ???
//      val url: String = GetEntityRequest[LineText]().queryURL(ref)
//
//      testGetEntityHelper(url, r => {r.toEither.left.get shouldBe a[InvalidUUIDinURLError] })
//
//    }
//
//  }
//
//}
