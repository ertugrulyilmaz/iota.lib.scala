package sota.utils

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{Matchers, WordSpec}

class ConverterTest extends WordSpec with Matchers with StrictLogging {

  val TEST_TRYTES = "BYSWE"
  val TEST_TRITS_1 =  Array(-1, 1, 0, 1, -1, 0, 1, 0, -1, -1, -1, 0, -1, -1, 1)
  val TEST_TRITS_2 =  Array(-1, 1, 0, 1, -1, 0, 1, 0, -1, -1, -1, 0, -1, -1, 1, 0, 0, 0, 0, 0)

  "Converter - bytes" should {

    "convert trits to byte array" in {
      Converter.bytes(TEST_TRITS_1) shouldBe Array(-52, -105, 44)
    }

    "convert trits to byte array specific position" in {
      Converter.bytes(TEST_TRITS_1, 0, 10) shouldBe Array(-52, -105)
    }

  }

  "Converter - trits" should {

    "copy to destionation array" in {
      val destionation = Array.ofDim[Int](TEST_TRITS_1.length)

      Converter.copyTrits(TEST_TRYTES, destionation) shouldBe TEST_TRITS_1
    }

    "convert trytes" in {
      Converter.trits(TEST_TRYTES) shouldBe TEST_TRITS_1
    }

    "convert trytes to specific length" in {
      Converter.trits(TEST_TRYTES, 20) shouldBe TEST_TRITS_2
    }

    "convert to string" in {
      Converter.tritsString(TEST_TRYTES) shouldBe TEST_TRITS_1
    }

    "convert trits to trytes string" in {
      Converter.trytes(TEST_TRITS_1) shouldBe TEST_TRYTES
    }

    "convert trits to trytes string from specific position" in {
      Converter.trytes(TEST_TRITS_1, 0, TEST_TRITS_1.length) shouldBe TEST_TRYTES
    }

    "convert tryte value" in {
      Converter.tryteValue(TEST_TRITS_1, 0) shouldBe 2
    }

    "convert value" in {
      Converter.value(TEST_TRITS_1) shouldBe 2572589
    }

    "convert long value" in {
      Converter.longValue(TEST_TRITS_1) shouldBe 2572589L
    }

  }

}
