package shared.communication.authentication

import shared.dataStorage.model.PWDHashed

object AdminPWD {
  val adminPWDSHA1 = "939fbd7fce4a4b362f05043500a9386983b92928"
  val testPWD= "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"
  val titokPWDSHA1="46ff53e764c4acf97b54db2020573049d2e3dab3"
  def getAdminPWDHash=PWDHashed(adminPWDSHA1)
}
