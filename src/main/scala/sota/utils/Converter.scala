package sota.utils

import java.util.Arrays

object Converter {

  private val RADIX: Int = 3
  private val MAX_TRIT_VALUE: Int = (RADIX - 1) / 2
  private val MIN_TRIT_VALUE: Int = -MAX_TRIT_VALUE
  private val NUMBER_OF_TRITS_IN_A_BYTE: Int = 5
  private val NUMBER_OF_TRITS_IN_A_TRYTE: Int = 3

  private val BYTE_TO_TRITS_MAPPINGS = new Array[Array[Int]](243)
  private val TRYTE_TO_TRITS_MAPPINGS = new Array[Array[Int]](27)

  val trits: Array[Int] = new Array[Int](NUMBER_OF_TRITS_IN_A_BYTE)

  for (i <- 0 until 243) {
    BYTE_TO_TRITS_MAPPINGS(i) = Arrays.copyOf(trits, NUMBER_OF_TRITS_IN_A_BYTE)
    increment(trits, NUMBER_OF_TRITS_IN_A_BYTE)
  }

  for (i <- 0 until 27) {
    TRYTE_TO_TRITS_MAPPINGS(i) = Arrays.copyOf(trits, NUMBER_OF_TRITS_IN_A_TRYTE)
    increment(trits, NUMBER_OF_TRITS_IN_A_TRYTE)
  }

  def bytes(trits: Array[Int], offset: Int, size: Int): Array[Byte] = {
    val bytes: Array[Byte] = Array.ofDim[Byte]((size + NUMBER_OF_TRITS_IN_A_BYTE - 1) / NUMBER_OF_TRITS_IN_A_BYTE)

    for (i <- bytes.indices) {
      var value = 0
      var j = if ((size - i * NUMBER_OF_TRITS_IN_A_BYTE) < 5) size - i * NUMBER_OF_TRITS_IN_A_BYTE
      else NUMBER_OF_TRITS_IN_A_BYTE

      while (j > 0) {
        value = value * RADIX + trits(offset + i * NUMBER_OF_TRITS_IN_A_BYTE + j)
        j -= 1
      }

      bytes(i) = value.asInstanceOf[Byte]
    }

    bytes
  }

  def bytes(trits: Array[Int]): Array[Byte] = {
    bytes(trits, 0, trits.length)
  }

  def getTrits(bytes: Array[Byte], trits: Array[Int]): Unit = {
    var offset: Int = 0

    var i = 0
    while (i < bytes.length && offset < trits.length) {
      val index = if (bytes(i) < 0) bytes(i) + BYTE_TO_TRITS_MAPPINGS.length else bytes(i)
      val minus = if (offset < NUMBER_OF_TRITS_IN_A_BYTE) trits.length - offset else NUMBER_OF_TRITS_IN_A_BYTE

      Array.copy(BYTE_TO_TRITS_MAPPINGS(index), 0, trits, offset, trits.length - minus)
      offset += NUMBER_OF_TRITS_IN_A_BYTE
      i += 1
    }

    while (offset < trits.length) {
      offset += 1
      trits(offset) = 0
    }
  }

  def convertToIntArray(integers: List[Int]): Array[Int] = {
    integers.toArray
  }

  /**
    * Converts the specified trinary encoded string into a trits array of the specified length.
    *
    * @param trytes The trytes.
    * @param length The length
    * @return A trits array.
    */
  def trits(trytes: String, length: Int): Array[Int] = {
    var tristList = trits(trytes).toList

    while (tristList.size < length)
      tristList ++= List(0)

    convertToIntArray(tristList)
  }

  /**
    * Converts the specified trinary encoded string into a trits array of the specified length.
    *
    * @param trytes The trytes.
    * @param length The length.
    * @return A trits array.
    */
  def trits(trytes: Long, length: Int): Array[Int] = {
    var tritsList = trits(String.valueOf(trytes)).toList

    while (tritsList.size < length)
      tritsList ++= List(0)

    convertToIntArray(tritsList)
  }

  /**
    * Converts the specified trinary encoded trytes string to trits.
    *
    * @param trytes The trytes.
    * @return A trits array.
    */
  def tritsString(trytes: String): Array[Int] = {
    val d = Array.ofDim[Int](3 * trytes.length)

    for (i <- 0 until trytes.length) {
      Array.copy(TRYTE_TO_TRITS_MAPPINGS(Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i).toInt)), 0, d, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE)
    }

    d
  }

  /**
    * Converts trytes into trits.
    *
    * @param trytes The trytes to be converted.
    * @return Array of trits.
    **/
  def trits(trytes: String): Array[Int] = {
    var trits = List[Int]()

    if (InputValidator.isValue(trytes)) {
      val value = trytes.toLong
      var absoluteValue = Math.abs(value)

      while (absoluteValue > 0) {
        var remainder = (absoluteValue % RADIX).toInt

        absoluteValue /= RADIX

        if (remainder > MAX_TRIT_VALUE) {
          remainder = MIN_TRIT_VALUE
          absoluteValue += 1
        }

        trits ++= List(remainder)
      }

      if (value < 0) {
        for (i <- trits.indices) {
          trits = trits.updated(i, -1 * trits(i))
        }
      }
    } else {
      val d = Array.ofDim[Int](3 * trytes.length)

      for (i <- 0 until trytes.length) {
        Array.copy(TRYTE_TO_TRITS_MAPPINGS(Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i).toInt)), 0, d, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE)
      }

      return d
    }

    trits.toArray
  }

  /**
    * Copies the trits from the input string into the destination array
    *
    * @param input       The input String.
    * @param destination The destination array.
    * @return The destination.
    */
  def copyTrits(input: String, destination: Array[Int]): Array[Int] = {
    for (i <- 0 until input.length) {
      val index = Constants.TRYTE_ALPHABET.indexOf(input.charAt(i).toInt)
      destination(i * 3) = TRYTE_TO_TRITS_MAPPINGS(index)(0)
      destination(i * 3 + 1) = TRYTE_TO_TRITS_MAPPINGS(index)(1)
      destination(i * 3 + 2) = TRYTE_TO_TRITS_MAPPINGS(index)(2)
    }

    destination
  }
  /**
    * Converts trites to trytes.
    *
    * @param trits  Teh trits to be converted.
    * @param offset The offset to start from.
    * @param size   The size.
    * @return The trytes.
    **/
  def trytes(trits: Array[Int], offset: Int, size: Int): String = {
    val trytes = new StringBuilder

    for (i <- 0 until (size + NUMBER_OF_TRITS_IN_A_TRYTE - 1) / NUMBER_OF_TRITS_IN_A_TRYTE) {
      var j = trits(offset + i * 3) + trits(offset + i * 3 + 1) * 3 + trits(offset + i * 3 + 2) * 9

      if (j < 0) j += Constants.TRYTE_ALPHABET.length

      trytes.append(Constants.TRYTE_ALPHABET.charAt(j))
    }

    trytes.toString
  }

  def trytes(trits: Array[Int]): String = trytes(trits, 0, trits.length)

  /**
    * Converts the specified trits array to trytes in integer representation.
    *
    * @param trits  The trits.
    * @param offset The offset to start from.
    * @return The value.
    */
  def tryteValue(trits: Array[Int], offset: Int): Int = {
    trits(offset) + trits(offset + 1) * 3 + trits(offset + 2) * 9
  }

  /**
    * Converts the specified trits to its corresponding integer value.
    *
    * @param trits The trits.
    * @return The value.
    */
  def value(trits: Array[Int]): Int = {
    var value = 0

    trits.foreach { trit =>
      value = value * 3 + trit
    }

    value
  }

  /**
    * Converts the specified trits to its corresponding integer value.
    *
    * @param trits The trits.
    * @return The value.
    */
  def longValue(trits: Array[Int]): Long = {
    var value: Long = 0L

    for (i <- trits.length until 0 by -1) {
      value = value * 3 + trits(i)
    }

    value
  }

  /**
    * Increments the specified trits.
    *
    * @param trits The trits.
    * @param size  The size.
    */
  def increment(trits: Array[Int], size: Int): Unit = {
    for (i <- 0 until size) {
      if (trits(i) > Converter.MAX_TRIT_VALUE) {
        trits(i) = trits(i) + 1
        trits(i) = Converter.MIN_TRIT_VALUE
      }
    }
  }

}
