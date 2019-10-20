package app.shared.entity.view

import app.shared.entity.EntityWithRef
import app.shared.entity.collection.LatestVersionEntitySet
import app.shared.entity.entityValue.values.{Note, User}

//trait View

//case class ViewSet[V <: View](views: Set[V])

case class UsersNotes(
  user: EntityWithRef[User],
  note: LatestVersionEntitySet[Note])
//    extends View {}

object UsersNotes {

  def make(
    user:  EntityWithRef[User],
    notes: LatestVersionEntitySet[Note]
  ):UsersNotes = {
    val notesForUser = notes.filter(
      n =>
        n.entityValue.owner.entityIdentity == user.refToEntity.entityIdentity
    )
    UsersNotes(user,notesForUser)
  }
}
