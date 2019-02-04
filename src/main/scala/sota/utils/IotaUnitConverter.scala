package sota.utils

import java.text.DecimalFormat

object IotaUnitConverter {

  def convertUnits(amount: Long, toUnit: IotaUnit): Long = {
    (amount / Math.pow(10, toUnit.value.toDouble)).asInstanceOf[Long]
  }

  def convertUnits(amount: Long, fromUnit: IotaUnit, toUnit: IotaUnit): Long = {
    val amountInSource = (amount * Math.pow(10, fromUnit.value.toDouble)).asInstanceOf[Long]

    convertUnits(amountInSource, toUnit)
  }

  def convertAmountTo(amount: Long, target: IotaUnit): Double = {
    amount / Math.pow(10, target.value.toDouble)
  }

  def createAmountWithUnitDisplayText(amountInUnit: Double, unit: IotaUnit, extended: Boolean): String = {
      createAmountDisplayText(amountInUnit, unit, extended) + " " + unit.unit
  }

  def createAmountDisplayText(amountInUnit: Double, unit: IotaUnit, extended: Boolean): String = {
    val df = if (extended) new DecimalFormat("##0.##################") else new DecimalFormat("##0.##")

    if (unit == IotaUnits.IOTA) amountInUnit.asInstanceOf[Long].toString else df.format(amountInUnit)
  }

  def findOptimalIotaUnitToDisplay(amount: Long): IotaUnit = {
    val length = if (amount < 0) String.valueOf(amount).length - 1 else String.valueOf(amount).length

    if (length >= 1 && length <= 3) {
      IotaUnits.IOTA
    } else if (length > 3 && length <= 6) {
      IotaUnits.KILO_IOTA
    } else if (length > 6 && length <= 9) {
      IotaUnits.MEGA_IOTA
    } else if (length > 9 && length <= 12) {
      IotaUnits.GIGA_IOTA
    } else if (length > 12 && length <= 15) {
      IotaUnits.TERA_IOTA
    } else if (length > 15 && length <= 18) {
      IotaUnits.PETA_IOTA
    } else {
      IotaUnits.IOTA
    }
  }

}
