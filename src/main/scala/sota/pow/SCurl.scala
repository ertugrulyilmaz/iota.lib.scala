package sota.pow

import java.util.Arrays

trait SCurl {

  lazy val HASH_LENGTH: Int = 243
  lazy val STATE_LENGTH: Int = 3 * HASH_LENGTH
  lazy val NUMBER_OF_ROUNDS: Int = 27
  lazy val TRUTH_TABLE: Array[Int] = Array(1, 0, -1, 1, -1, 0, -1, 1, 0)
  lazy val state = Array.ofDim[Int](STATE_LENGTH)

  def absorb(trits: Array[Int], offset: Int, length: Int): SCurl = {
    var l = length
    var o = offset

    do {
      val copyLength = if (length < HASH_LENGTH) length else HASH_LENGTH
      Array.copy(trits, offset, state, 0, copyLength)

      transform()

      o += HASH_LENGTH
      l -= HASH_LENGTH
    } while (l > 0)

    this
  }

  def absorb(trits: Array[Int]): SCurl = {
    absorb(trits, 0, trits.length)
  }

  def squeeze(trits: Array[Int], offset: Int, length: Int): Array[Int] = {
    var l = length
    var o = offset

    do {
      val copyLength = if (length < HASH_LENGTH) length else HASH_LENGTH
      Array.copy(trits, offset, state, 0, copyLength)

      transform()

      o += HASH_LENGTH
      l -= HASH_LENGTH
    } while (l > 0)

    state
  }

  def squeeze(trits: Array[Int]): Array[Int] = {
    squeeze(trits, 0, trits.length)
  }

  def transform(): SCurl = {
    val scratchpad = Array.ofDim[Int](STATE_LENGTH)
    var scratchpadIndex = 0
    for (round <- 0 until NUMBER_OF_ROUNDS) {
      Array.copy(state, 0, scratchpad, 0, STATE_LENGTH)

      for (stateIndex <- 0 until STATE_LENGTH) {
        val firstIndex = scratchpad(scratchpadIndex)

        scratchpadIndex += (if (scratchpadIndex < 365) 364 else -365)

        val secondIndex = scratchpad(scratchpadIndex) * 3 + 4

        state(stateIndex) = TRUTH_TABLE(firstIndex + secondIndex)
      }
    }

    this
  }

  def reset(): SCurl = {
    Arrays.fill(state, 0)

    this
  }

  override def clone(): SCurl = {
    SCurl()
  }

}

object SCurl {

  def apply(): SCurl = new SCurl {
  }

}
