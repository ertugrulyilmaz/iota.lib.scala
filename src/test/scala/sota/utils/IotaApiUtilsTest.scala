package sota.utils

import org.scalatest.{Matchers, WordSpec}
import sota.pow.SCurl

class IotaApiUtilsTest extends WordSpec with Matchers {

  val TEST_SEED1 = "AAA999999999999999999999999999999999999999999999999999999999999999999999999999999"

  "IotaApiUtils - newAddress" should {

    "return valid address if address valid" in {
      val curl = SCurl()
      val expectedAddress = "NINSZRAKWBERQBBN9KGIRNXQDNENLBUBAYRZPUXFJSWRWVADEOGGOWMLQWSHA9NEOLASWRGOQJXAVRMFY"
      IotaAPIUtils.newAddress(TEST_SEED1, 1, 0, false, curl) shouldBe expectedAddress
    }

  }

}
