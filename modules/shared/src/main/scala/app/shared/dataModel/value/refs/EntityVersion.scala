package app.shared.dataModel.value.refs

case class EntityVersion(versionNumberLong: Long = 0 ) {

  def inc(): EntityVersion =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
