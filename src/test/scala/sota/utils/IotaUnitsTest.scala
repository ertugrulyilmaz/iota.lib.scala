package sota.utils

import org.scalatest.{Matchers, WordSpec}

class IotaUnitsTest extends WordSpec with Matchers {

  "IotaUnits - " should {

    "create correct unit type" in {
      val unit = IotaUnits.IOTA
      val unitK = IotaUnits.KILO_IOTA
      val unitM = IotaUnits.MEGA_IOTA
      val unitG = IotaUnits.GIGA_IOTA
      val unitT = IotaUnits.TERA_IOTA
      val unitP = IotaUnits.PETA_IOTA

      unit.unit shouldBe "i"
      unit.value shouldBe 0

      unitK.unit shouldBe "Ki"
      unitK.value shouldBe 3

      unitM.unit shouldBe "Mi"
      unitM.value shouldBe 6

      unitG.unit shouldBe "Gi"
      unitG.value shouldBe 9

      unitT.unit shouldBe "Ti"
      unitT.value shouldBe 12

      unitP.unit shouldBe "Pi"
      unitP.value shouldBe 15
    }

  }

}
