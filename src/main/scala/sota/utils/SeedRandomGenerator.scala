package sota.utils

import java.security.SecureRandom

object SeedRandomGenerator {

  lazy val CHARS = Constants.TRYTE_ALPHABET.toCharArray

  /**
    * Generate a new seed.
    *
    * @return Random generated seed.
    **/
  def generateNewSeed(): String = {
    val random = new SecureRandom()

    (0 until Constants.SEED_LENGTH_MAX).map { _ =>
      CHARS(random.nextInt(CHARS.length))
    }.mkString("")
  }

}
