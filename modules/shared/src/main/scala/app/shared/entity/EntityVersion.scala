package app.shared.entity

case class EntityVersion(versionNumberLong: Long = 0 ) {

  def inc(): EntityVersion =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
