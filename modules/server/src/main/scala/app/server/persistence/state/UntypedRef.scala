package app.server.persistence.state

import app.shared.dataModel.entity.EntityTypeAsString

case class UntypedRef(uuid:String,version:Long,entityType:EntityTypeAsString)
