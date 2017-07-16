package sota.utils

object TrytesConverter {
  /**
    * Conversion of ascii encoded bytes to trytes.
    * Input is a string (can be stringified JSON object), return value is Trytes
    * <p>
    * How the conversion works:
    * 2 Trytes === 1 Byte
    * There are a total of 27 different tryte values: 9ABCDEFGHIJKLMNOPQRSTUVWXYZ
    * <p>
    * 1. We get the decimal value of an individual ASCII character
    * 2. From the decimal value, we then derive the two tryte values by basically calculating the tryte equivalent (e.g. 100 === 19 + 3 * 27)
    * a. The first tryte value is the decimal value modulo 27 (27 trytes)
    * b. The second value is the remainder (decimal value - first value), divided by 27
    * 3. The two values returned from Step 2. are then input as indices into the available values list ('9ABCDEFGHIJKLMNOPQRSTUVWXYZ') to get the correct tryte value
    * <p>
    * <p>
    * EXAMPLE
    * <p>
    * Lets say we want to convert the ASCII character "Z".
    * 1. 'Z' has a decimal value of 90.
    * 2. 90 can be represented as 9 + 3 * 27. To make it simpler:
    * a. First value: 90 modulo 27 is 9. This is now our first value
    * b. Second value: (90 - 9) / 27 is 3. This is our second value.
    * 3. Our two values are now 9 and 3. To get the tryte value now we simply insert it as indices into '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    * a. The first tryte value is '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'[9] === "I"
    * b. The second tryte value is '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'[3] === "C"
    * Our tryte pair is "IC"
    *
    * @param inputString The input String.
    * @return The ASCII char "Z" is represented as "IC" in trytes.
    */
  def toTrytes(inputString: String): String = {
    inputString.toCharArray.map {
      case c if c.toInt == 255 => 32
      case c => c.toInt
    }.map { asciiValue =>
      val firstValue = asciiValue % 27
      val secondValue = (asciiValue - firstValue) / 27

      String.valueOf(Constants.TRYTE_ALPHABET.charAt(firstValue)) + String.valueOf(Constants.TRYTE_ALPHABET.charAt(secondValue))
    }.mkString("")
  }

  /**
    * Trytes to bytes
    * Reverse operation from the byteToTrytes function in send.js
    * 2 Trytes == 1 Byte
    * We assume that the trytes are a JSON encoded object thus for our encoding:
    * First character = {
    * Last character = }
    * Everything after that is 9's padding
    */
  def toString(inputTrytes: String): String = {
    val sb = new StringBuilder
    for (i <- 0 until inputTrytes.length by 2) {
      val firstValue = Constants.TRYTE_ALPHABET.indexOf(inputTrytes.charAt(i).toInt)
      val secondValue = Constants.TRYTE_ALPHABET.indexOf(inputTrytes.charAt(i + 1).toInt)

      val decimalValue = firstValue + secondValue * 27
      sb.append(Character.toString(decimalValue.asInstanceOf[Char]))
    }

    sb.toString
  }

}
