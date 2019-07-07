package app.shared.data.ref

import app.shared.TypeError
import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.model.TypeAsString
import app.shared.data.ref.unTyped.NotTypeSafeRef
import app.shared.data.ref.UUID_Utils.UUIDCompare
import monocle.macros.Lenses

import scala.reflect.ClassTag
import scalaz.{-\/, \/}
//import shapeless.Typeable

case class Version(l: Long = 0 ) {
  def inc(): Version = this.copy( l = this.l + 1 )
}

/**
  * Created by joco on 28/04/2017.
  */
@Lenses
case class RefVal[T <: Entity](r: TypedRef[T], v: T, version: Version ) {
  def map(f: T => T ): RefVal[T] = copy( v = f( v ) )

  override def toString: String = super.toString
}

object RefVal {
  //  def create[T](v:T)=RefVal(Ref(),v)
  //  def apply[T](r:Ref[T],v:T)=new RefVal(r,v)
  //      def make[T<:Entity[T]](v: T) = new RefVal(Ref[T](), v)
  //    def make[T<:Entity[T]](v: T)(implicit t:Typeable[T]) = new RefVal(Ref.make[T](), v)

  implicit def instance[T <: Entity]: UUIDCompare[RefVal[T]] =
    (x: RefVal[T], y: RefVal[T]) => TypedRef.instance.isUUIDEq(x.r, y.r)

}

//TODOlater - get rid of this ASAP
case class RefValDyn(r: NotTypeSafeRef, e: Entity, version: Version ) {

  def toRefVal[E <: Entity: ClassTag]: \/[TypeError, RefVal[E]] = {
    // f0c1cede98f0430c85f35944546bbba4w
    val et = TypeAsString.make[E]
    if (et == r.et) {
      val etyped:  E                     = e.asInstanceOf[E]
      val refDisj: \/[TypeError, TypedRef[E]] = r.toRef[E]()
      refDisj.map( RefVal( _, etyped, version ) )
    } else -\/( TypeError( "RefValDyn.toRefVal " ) )
  }

  def toRefVal_NoClassTagNeeded[E <: Entity](expectedEntityType: TypeAsString ): \/[TypeError, RefVal[E]] = {
    // f0c1cede98f0430c85f35944546bbba4w
    if (expectedEntityType == r.et) {
      val etyped:  E                     = e.asInstanceOf[E]
      val refDisj: \/[TypeError, TypedRef[E]] = r.toRef_noClassTagNeeded( expectedEntityType )
      refDisj.map( RefVal( _, etyped, version ) )
    } else -\/( TypeError( "RefValDyn.toRefVal " ) )
  }

//  def toRefValNoCheck[E<:Entity] : RefVal[E] = {
//      val etyped:E=e.asInstanceOf[E]
//      RefVal(r.toRefUnsafe[E](), etyped)
//  }
//override def toString: String = PrettyPrint.prettyPrint(this)

}

object RefValDyn {

//  def makeDefauldRefValDynForEntity(entityType: EntityType): RefValDyn = {
//    val e: Entity = EntityType.makeDefaultEntity(entityType)
//    val rd = RefDyn.make(entityType)
//    val rvd = RefValDyn(rd, e)
//    rvd
//  }

  def makeRefValDynForNewlyCreatedEntity(
      ent: Entity
    ): RefValDyn = { // assumes that all parameters are correct
    val et  = TypeAsString.fromEntity(ent)
    val rd  = NotTypeSafeRef.make( et )
    val rvd = RefValDyn( rd, ent, Version() )
    rvd
  }

  implicit def fromRefValToRefValDyn[E <: Entity](rv: RefVal[E] ): RefValDyn = {
    val rd = new NotTypeSafeRef( rv.r.uuid, rv.r.dataType )
    new RefValDyn( rd, rv.v, rv.version )
  }

}
