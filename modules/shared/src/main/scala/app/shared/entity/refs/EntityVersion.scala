package app.shared.entity.refs

import monocle.macros.Lenses

@Lenses
case class EntityVersion(versionNumberLong: Long = 0 ) {

  def bumpVersion(): EntityVersion =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
