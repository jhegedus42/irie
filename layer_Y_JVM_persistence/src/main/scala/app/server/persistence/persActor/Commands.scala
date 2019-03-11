package app.server.persistence.persActor

import app.server.persistence.ApplicationState
import app.shared.SomeError_Trait
import app.shared.data.model.Entity.Data
import app.shared.data.ref.RefValDyn
import app.testHelpersShared.data.TestDataLabels.TestDataLabel
import scalaz.\/

/**
  *
  * Ezek parancsok és visszaérkező válaszok,
  * amiket a Persistent Actor-nak lehet küldeni.
  *
  * És ki küldi őket ?
  *
  */
object Commands {
  // Random UUID: de5e8eabb1314941bebe81f57c82257b
  // commit 261bba625a6dc3bfc178a1d578cd104b23cf6437
  // Date: Tue Aug  7 08:52:23 EEST 2018

  //  protocol

  case class UpdateEntityPACommand(entity: RefValDyn)
  case class UpdateEntityPAResponse(payload: \/[SomeError_Trait, RefValDyn])

  case class CreateEntityPACommand[E <: Data](e:Data)
  case class CreateEntityPAResponse(payload: \/[SomeError_Trait, RefValDyn])

  case object GetStatePACommand
  case class GetStatePAResponse(state: ApplicationState)

  case class SetStatePACommand(tdl:TestDataLabel)
  case class SetStatePAResponse(success: Boolean)
}
