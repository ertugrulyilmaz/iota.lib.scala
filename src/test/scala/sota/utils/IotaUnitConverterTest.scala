package sota.utils

import org.scalatest.{Matchers, WordSpec}

class IotaUnitConverterTest extends WordSpec with Matchers {

  "IotaUnitConverter - convertAmountTo" should {

    "return long value of iotaunit" in {
      val amount = IotaUnitConverter.convertAmountTo(105L, IotaUnits.IOTA)
      val amountK = IotaUnitConverter.convertAmountTo(105000L, IotaUnits.KILO_IOTA)
      val amountM = IotaUnitConverter.convertAmountTo(105000000L, IotaUnits.MEGA_IOTA)
      val amountG = IotaUnitConverter.convertAmountTo(105000000000L, IotaUnits.GIGA_IOTA)
      val amountT = IotaUnitConverter.convertAmountTo(105000000000000L, IotaUnits.TERA_IOTA)
      val amountP = IotaUnitConverter.convertAmountTo(105000000000000000L, IotaUnits.PETA_IOTA)
      amount shouldBe 105
      amountK shouldBe 105
      amountM shouldBe 105
      amountG shouldBe 105
      amountT shouldBe 105
      amountP shouldBe 105
    }

  }

  "IotaUnitConverter - convertUnits" should {

    "return from unit to other unit" in {
      val amount = IotaUnitConverter.convertUnits(1000L, IotaUnits.IOTA, IotaUnits.KILO_IOTA)
      val amountK = IotaUnitConverter.convertUnits(1000L, IotaUnits.KILO_IOTA, IotaUnits.MEGA_IOTA)
      val amountM = IotaUnitConverter.convertUnits(1000L, IotaUnits.MEGA_IOTA, IotaUnits.GIGA_IOTA)
      val amountG = IotaUnitConverter.convertUnits(1000L, IotaUnits.GIGA_IOTA, IotaUnits.TERA_IOTA)
      val amountT = IotaUnitConverter.convertUnits(1000L, IotaUnits.TERA_IOTA, IotaUnits.PETA_IOTA)
      amount shouldBe 1L
      amountK shouldBe 1L
      amountM shouldBe 1L
      amountG shouldBe 1L
      amountT shouldBe 1L
    }

  }

  "IotaUnitConverter - convertRawIotaAmountToDisplayText" should {

    "return value with unit text" in {
      val amount = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.IOTA, false)
      val amountK = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.KILO_IOTA, false)
      val amountM = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.MEGA_IOTA, false)
      val amountG = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.GIGA_IOTA, false)
      val amountT = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.TERA_IOTA, false)
      val amountP = IotaUnitConverter.createAmountWithUnitDisplayText(1, IotaUnits.PETA_IOTA, false)
      amount shouldBe "1 i"
      amountK shouldBe "1 Ki"
      amountM shouldBe "1 Mi"
      amountG shouldBe "1 Gi"
      amountT shouldBe "1 Ti"
      amountP shouldBe "1 Pi"
    }

  }

  "IotaUnitConverter - createAmountDisplayText" should {

    "return value if extended false" in {
      val amount = IotaUnitConverter.createAmountDisplayText(1.1, IotaUnits.IOTA, false)
      val amountK = IotaUnitConverter.createAmountDisplayText(1.1234, IotaUnits.KILO_IOTA, false)
      val amountM = IotaUnitConverter.createAmountDisplayText(1.1234, IotaUnits.MEGA_IOTA, false)
      val amountG = IotaUnitConverter.createAmountDisplayText(1.1234, IotaUnits.GIGA_IOTA, false)
      val amountT = IotaUnitConverter.createAmountDisplayText(1.1234, IotaUnits.TERA_IOTA, false)
      val amountP = IotaUnitConverter.createAmountDisplayText(1.1234, IotaUnits.PETA_IOTA, false)
      amount shouldBe "1"
      amountK shouldBe "1.12"
      amountM shouldBe "1.12"
      amountG shouldBe "1.12"
      amountT shouldBe "1.12"
      amountP shouldBe "1.12"
    }

    "return extended value" in {
      val amount = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.IOTA, true)
      val amountK = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.KILO_IOTA, true)
      val amountM = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.MEGA_IOTA, true)
      val amountG = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.GIGA_IOTA, true)
      val amountT = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.TERA_IOTA, true)
      val amountP = IotaUnitConverter.createAmountDisplayText(1.123456, IotaUnits.PETA_IOTA, true)
      amount shouldBe "1"
      amountK shouldBe "1.123456"
      amountM shouldBe "1.123456"
      amountG shouldBe "1.123456"
      amountT shouldBe "1.123456"
      amountP shouldBe "1.123456"
    }

  }

}
