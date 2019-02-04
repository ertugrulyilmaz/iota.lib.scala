package sota.utils

import sota.error.InvalidAddressException
import sota.pow.SCurl

/**
  * This class defines utility methods to add/remove the checksum to/from an address.
  */
object Checksum {

  /**
    * Adds the checksum to the specified address.
    *
    * @param address The address without checksum.
    * @return The address with the appended checksum.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address.
    **/
  @throws(classOf[InvalidAddressException])
  def addChecksum(address: String): String = {
    InputValidator.checkAddress(address)

    address + calculateChecksum(address)
  }

  /**
    * Remove the checksum to the specified address.
    *
    * @param address The address with checksum.
    * @return The address without checksum.
    * @throws InvalidAddressException is thrown when the specified address is not an address with checksum.
    **/
  @throws(classOf[InvalidAddressException])
  def removeChecksum(address: String): String = {
    if (isAddressWithChecksum(address)) {
      return removeChecksumFromAddress(address)
    } else if (isAddressWithoutChecksum(address)) {
      return address
    }

    throw new InvalidAddressException()
  }

  private def removeChecksumFromAddress(addressWithChecksum: String): String =
    addressWithChecksum.substring(0, Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM)

  /**
    * Determines whether the specified address with checksum has a valid checksum.
    *
    * @param addressWithChecksum The address with checksum.
    * @return <code>true</code> if the specified address with checksum has a valid checksum [the specified address with checksum]; otherwise, <code>false</code>.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address.
    **/
  @throws(classOf[InvalidAddressException])
  def isValidChecksum(addressWithChecksum: String): Boolean = {
    val addressWithoutChecksum = removeChecksum(addressWithChecksum)

    (addressWithoutChecksum + calculateChecksum(addressWithoutChecksum)) == addressWithChecksum
  }

  /**
    * Check if specified address is a address with checksum.
    *
    * @param address The address to check.
    * @return <code>true</code> if the specified address is with checksum ; otherwise, <code>false</code>.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address
    **/
  @throws(classOf[InvalidAddressException])
  def isAddressWithChecksum(address: String): Boolean =
    InputValidator.checkAddress(address) && address.length == Constants.ADDRESS_LENGTH_WITH_CHECKSUM

  /**
    * check if specified address is a address
    *
    * @param address address
    * @return boolean
    * @throws InvalidAddressException is thrown when the specified address is not an valid address
    **/
  @throws(classOf[InvalidAddressException])
  def isAddressWithoutChecksum(address: String): Boolean =
    InputValidator.checkAddress(address) && address.length == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM

  private def calculateChecksum(address: String): String = {
    val curl = SCurl()

    curl.reset()
    val state = Converter.copyTrits(address, curl.state)
    Array.copy(state, 0, curl.state, 0, state.length)
    curl.transform()

    Converter.trytes(curl.state).substring(0, 9)
  }

}
