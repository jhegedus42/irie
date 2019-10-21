package app.shared.entity.view

import app.shared.entity.EntityWithRef
import app.shared.entity.collection.LatestVersionEntitySet
import app.shared.entity.entityValue.values.{Note, User}

//trait View

//case class ViewSet[V <: View](views: Set[V])
import io.circe._
import monocle.macros.Lenses

import scala.reflect.ClassTag

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

//@JsonCodec
//@Lenses
//case class UsersNotes(
//  user: EntityWithRef[User],
//  note: LatestVersionEntitySet[Note])
////    extends View {}
//
//object UsersNotes {
//
//  def make(
//    user:  EntityWithRef[User],
//    notes: LatestVersionEntitySet[Note]
//  ):UsersNotes = {
//    val notesForUser = notes.filter(
//      n =>
//        n.entityValue.owner.entityIdentity == user.refToEntity.entityIdentity
//    )
//    UsersNotes(user,notesForUser)
//  }
//}
