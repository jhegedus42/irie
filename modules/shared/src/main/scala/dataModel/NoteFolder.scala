package dataModel

import io.circe.generic.JsonCodec
import refs.{EntityType, RefToEntityWithVersion}

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(
  user: RefToEntityWithVersion[User],
  name: String)
    extends EntityType[NoteFolder]
