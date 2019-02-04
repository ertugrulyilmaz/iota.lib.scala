package sota.utils

import org.scalatest.{Matchers, WordSpec}
import sota.error.InvalidAddressException

class ChecksumTest extends WordSpec with Matchers {

  val TEST_INVALID_ADDRESS = "2AVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVA2"
  val TEST_ADDRESS_WITHOUT_CHECKSUM = "AVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVA"
  val TEST_ADDRESS_WITH_CHECKSUM = "AVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAELHILPAAF"

  "Checksum - addChecksum" should {

    "throw exception when address is invalid" in {
      intercept[InvalidAddressException] {
        Checksum.addChecksum(TEST_INVALID_ADDRESS)
      }
    }

    "run only valid address" in {
      Checksum.addChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM) shouldBe TEST_ADDRESS_WITH_CHECKSUM
    }

  }

  "Checksum - removeChecksum" should {

    "throw exception when address is invalid" in {
      intercept[InvalidAddressException] {
        Checksum.removeChecksum(TEST_INVALID_ADDRESS)
      }
    }

    "remove checksum if address has checksum" in {
      Checksum.removeChecksum(TEST_ADDRESS_WITH_CHECKSUM) shouldBe TEST_ADDRESS_WITHOUT_CHECKSUM
    }

    "return same address if address has not checksum" in {
      Checksum.removeChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM) shouldBe TEST_ADDRESS_WITHOUT_CHECKSUM
    }

  }

  "Checksum - isValidChecksum" should {

    "throw exception when address is invalid" in {
      intercept[InvalidAddressException] {
        Checksum.isValidChecksum(TEST_INVALID_ADDRESS)
      }
    }

    "return true when address has valid checksum" in {
      Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM) shouldBe true
    }

  }

  "Checksum - isAddressWithChecksum" should {

    "throw exception when address is invalid" in {
      intercept[InvalidAddressException] {
        Checksum.isAddressWithChecksum(TEST_INVALID_ADDRESS)
      }
    }

    "return true when address has checksum" in {
      Checksum.isAddressWithChecksum(TEST_ADDRESS_WITH_CHECKSUM) shouldBe true
    }

  }

  "Checksum - isAddressWithoutChecksum" should {

    "throw exception when address is invalid" in {
      intercept[InvalidAddressException] {
        Checksum.isAddressWithoutChecksum(TEST_INVALID_ADDRESS)
      }
    }

    "return true when address has not checksum" in {
      Checksum.isAddressWithoutChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM) shouldBe true
    }

  }

}
