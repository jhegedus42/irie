package dataModel

import io.circe.generic.JsonCodec
import refs.{EntityType, IdentityAndVersion}

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(
                       user: IdentityAndVersion[User],
                       name: String)
    extends EntityType[NoteFolder]
