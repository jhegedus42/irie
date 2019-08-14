package app.shared.dataModel.entity.refs

case class Version( versionNumberLong: Long = 0 ) {

  def inc(): Version =
    this.copy( versionNumberLong = this.versionNumberLong + 1 )
}
