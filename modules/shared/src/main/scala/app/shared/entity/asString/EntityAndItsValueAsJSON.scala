package app.shared.entity.asString

import monocle.macros.Lenses

@Lenses
case class EntityAndItsValueAsJSON(
    entityAsJSON:      EntityAsJSON,
    entityValueAsJSON: EntityValueAsJSON
)
