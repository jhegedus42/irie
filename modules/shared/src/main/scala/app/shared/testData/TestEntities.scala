package app.shared.testData

import app.shared.entity.{Note, TypedRef, TypedRefVal, Version}

/**
  * Created by joco on 09/10/2017.
  */
object TestEntities {
  val theUUIDofTheLine           = "4ce6fca0-0fd5-4197-a946-90f5e7e00d9d"
  val line                       = Note( content = "text" )

  val refToLine: TypedRef[Note] =
    TypedRef.makeWithUUID[Note]( theUUIDofTheLine )

  val refValOfLineV0 = TypedRefVal( refToLine, line, Version() )


}
