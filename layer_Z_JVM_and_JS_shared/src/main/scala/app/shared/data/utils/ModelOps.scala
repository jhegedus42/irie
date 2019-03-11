//package app.shared.data.utils
//
//import app.shared.data.ref.uuid.UUIDCompare
//
///**
//  * Created by joco on 28/04/2017.
//  */
//object ModelOps{ // operations on the model
//  def getNext[T:UUIDCompare](seq:Seq[T], element:T):Option[T]={
//    def uuidEq: (T,T) => Boolean = implicitly[UUIDCompare[T]].isUUIDEq
//    val f=(x:T) => !(uuidEq(x,element))
//    val l= seq.dropWhile(f).drop(1).headOption
//    println(s"debug ModelOps ls=$seq l=$l");
//    l
//  }
//  def getPrev[T:UUIDCompare](seq:Seq[T], element:T):Option[T]={
//    def uuidEq: (T,T) => Boolean = implicitly[UUIDCompare[T]].isUUIDEq
//    val f=(x:T) => !(uuidEq(x,element))
//    val l= seq.takeWhile(f).lastOption
//    println(s"debug ModelOps ls=$seq l=$l");
//    l
//  }
//
//}
