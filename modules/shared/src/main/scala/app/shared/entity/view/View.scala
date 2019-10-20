package app.shared.entity.view

import app.shared.entity.entityValue.values.{Note, User}

trait View

case class ViewSet[V<:View](views:Set[V])

case class UsersNotes(user:User,note:Note) extends View
