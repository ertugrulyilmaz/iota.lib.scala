package sota.utils

import sota.error.InvalidAddressException
import sota.pow.SCurl

object Checksum {

  @throws(classOf[InvalidAddressException])
  def addChecksum(address: String): String = {
    InputValidator.checkAddress(address)

    address + calculateChecksum(address)
  }

  @throws(classOf[InvalidAddressException])
  def removeChecksum(address: String): String = {
    if (isAddressWithChecksum(address)) {
      return removeChecksumFromAddress(address)
    } else if (isAddressWithoutChecksum(address)) {
      return address
    }

    throw new InvalidAddressException()
  }

  def removeChecksumFromAddress(addressWithChecksum: String): String = {
    addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)
  }

  @throws(classOf[InvalidAddressException])
  def isValidChecksum(addressWithChecksum: String): Boolean = {
    var temp = addressWithChecksum
    val addressWithoutChecksum = removeChecksum(temp)

    temp += calculateChecksum(addressWithoutChecksum)

    val addressWithRecalculateChecksum = temp

    addressWithRecalculateChecksum == temp
  }

  @throws(classOf[InvalidAddressException])
  def isAddressWithChecksum(address: String): Boolean = {
    InputValidator.checkAddress(address) && address.length == Constants.ADDRESS_LENGTH_WITH_CHECKSUM
  }

  @throws(classOf[InvalidAddressException])
  def isAddressWithoutChecksum(address: String): Boolean = {
    InputValidator.checkAddress(address) && address.length == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM
  }

  def calculateChecksum(address: String): String = {
    val curl = SCurl()
    val newState = Converter.copyTrits(address, curl.state)

    curl.reset()
    Array.copy(newState, 0, curl.state, 0, newState.length)
    curl.transform()

    Converter.trytes(curl.state).substring(0, 9)
  }

}
