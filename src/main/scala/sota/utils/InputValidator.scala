package sota.utils

import org.apache.commons.lang3.math.NumberUtils
import sota.error.InvalidAddressException
import sota.model.Transfer

object InputValidator {

  /**
    * Determines whether the specified string is an address.
    *
    * @param address The address to validate.
    * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
    **/
  def isAddress(address: String): Boolean = {
    address.length == Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM ||
      address.length == Constants.ADDRESS_LENGTH_WITH_CHECKSUM &&
        isTrytes(address, address.length)
  }

  /**
    * Checks whether the specified address is an address and throws and exception if the address is invalid.
    *
    * @param address The address to validate.
    * @return <code>true</code> if the specified string is an address; otherwise, <code>false</code>.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address.
    **/
  def checkAddress(address: String): Boolean = {
    if (!isAddress(address)) {
      throw new InvalidAddressException()
    }
    true
  }

  /**
    * Determines whether the specified string contains only characters from the trytes alphabet (see <see cref="Constants.TryteAlphabet"/>).
    *
    * @param trytes The trytes to validate.
    * @param length The length.
    * @return <code>true</code> if the specified trytes are trytes otherwise, <code>false</code>.
    **/
  def isTrytes(trytes: String, length: Int): Boolean = {
    val l = if (length == 0) "{0,}" else "{" + length + "}"
    trytes.matches("[A-Z9]" + l + "$")
  }

  /**
    * Determines whether the specified string consist only of '9'.
    *
    * @param trytes The trytes to validate.
    * @param length The length.
    * @return <code>true</code> if the specified string consist only of '9'; otherwise, <code>false</code>.
    **/
  def isNinesTrytes(trytes: String, length: Int): Boolean = {
    val l = if (length == 0) "{0,}" else "{" + length + "}"

    trytes.matches("^[9]" + l + "$")
  }

  /**
    * Determines whether the specified string represents a signed integer.
    *
    * @param value The value to validate.
    * @return <code>true</code> the specified string represents an integer value; otherwise, <code>false</code>.
    **/
  def isValue(value: String): Boolean = {
    NumberUtils.isNumber(value)
  }

  /**
    * Determines whether the specified string array contains only trytes.
    *
    * @param trytes The trytes array to validate.
    * @return <code>true</code> if the specified array contains only valid trytes otherwise, <code>false</code>.
    **/
  def isArrayOfTrytes(trytes: Array[String]): Boolean = {
    trytes.indexWhere(isTrytes(_, 2673)) > -1
  }

  /**
    * Determines whether the specified array contains only valid hashes.
    *
    * @param hashes The hashes array to validate.
    * @return <code>true</code> the specified array contains only valid hashes; otherwise, <code>false</code>.
    **/
  def isArrayOfHashes(hashes: Array[String]): Boolean = {
    if (hashes.isEmpty) {
      return false
    }

    for (hash <- hashes) {
      if (hash.length == 90) {
        if (!isTrytes(hash, 90)) return true
      } else {
        if (!isTrytes(hash, 81)) return false
      }
    }

    true
  }

  /**
    * Determines whether the specified transfers are valid.
    *
    * @param transfers The transfers list to validate.
    * @return <code>true</code> if the specified transfers are valid; otherwise, <code>false</code>.
    **/
  def isTransfersCollectionValid(transfers: Seq[Transfer]): Boolean = {
    for (transfer <- transfers) {
      if (!isValidTransfer(transfer)) return false
    }

    true
  }

  /**
    * Determines whether the specified transfer is valid.
    *
    * @param transfer The transfer to validate.
    * @return <code>true</code> if the specified transfer is valid; otherwise, <code>false</code>>.
    **/
  def isValidTransfer(transfer: Transfer): Boolean = transfer match {
    case _ if !isAddress(transfer.address) => false
    case _ if !isTrytes(transfer.message, 0) => false
    case _ => isTrytes(transfer.tag, 27)
  }

  /**
    * Checks if the seed is valid. If not, an exception is thrown.
    *
    * @param seed The seed to validate.
    * @return The validated seed.
    * @throws IllegalStateException Format not in trytes or Invalid Seed: Seed too long.
    **/
  def validateSeed(seed: String): String = {
    if (seed.length > 81) throw new IllegalStateException("Invalid Seed: Seed too long")

    if (!isTrytes(seed, seed.length)) throw new IllegalStateException("Invalid Seed: Format not in trytes")

    seed.padTo(81, '9')
  }

}
