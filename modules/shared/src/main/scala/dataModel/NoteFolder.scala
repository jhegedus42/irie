package dataModel

import entity.Ref


case class NoteFolder(
                       user: Ref[User],
                       name: String)
    extends EntityValueType[NoteFolder]
