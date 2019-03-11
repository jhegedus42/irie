package app.shared.data.utils

//import app.shared.data.utils.model.LineOld
//import app.shared.data.ref.{Ref, RefVal}
//import app.shared.data.utils.ModelOps
//import org.specs2.mutable.Specification
//
//import scala.collection.immutable.Seq


//
///**
//  * Created by joco on 17/03/17.
//  */
//class ModelOps$Test  extends Specification {
//
//  object TestDataForModel {
////    implicit object CanCreate extends OnlyServerCanCreateEntity
//
//    val l1= LineOld("egy")
//    val l2= LineOld("ketto")
//    val l3= LineOld("harom")
//    val l4= LineOld("negy")
//    val l5= LineOld("ot")
//
//    val lbla= LineOld("bla")
//
//    val testListInputRefVal: Seq[RefVal[LineOld]] = List(l1, l2, l3, l4, l5).map(RefVal.make(_))
//    val testListInputRefs: Seq[Ref[LineOld]]      = testListInputRefVal.map(_.r)
//
//  }
//
//  import TestDataForModel._
//
//
//  "getNextLine on l1" should {
//    val gl = ModelOps.getNext(testListInputRefs, testListInputRefs(0))
//    gl === Some(testListInputRefs(1))
//  }
//
//  "getNextLine last" should {
//    val gl = ModelOps.getNext(testListInputRefs, testListInputRefs(4))
//    gl === None
//  }
//
//  "getNextLine on empty" should {
//    val gl = ModelOps.getNext(List(), testListInputRefs(4))
//    gl === None
//  }
//  "getPrevLine on l1" should {
//    val gl = ModelOps.getPrev(testListInputRefs, testListInputRefs(1))
//    gl === Some(testListInputRefs(0))
//  }
//
//  "getPrevLine last" should {
//    val gl = ModelOps.getPrev(testListInputRefs, testListInputRefs(0))
//    gl === None
//  }
//
//  "getPrevLine last" should {
//    val gl = ModelOps.getPrev(testListInputRefs, testListInputRefs(1))
//    gl !== None
//  }
//
//  "getPrevLine on empty" should {
//    val gl = ModelOps.getPrev(List(), testListInputRefs(4))
//    gl === None
//  }
//
//}