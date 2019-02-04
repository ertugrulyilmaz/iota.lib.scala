package sota.utils

sealed trait IotaUnit {
  val unit: String
  val value: Long
}

object IotaUnits {
  case object IOTA extends IotaUnit {
    override val unit: String = "i"
    override val value: Long = 0
  }

  case object KILO_IOTA extends IotaUnit {
    override val unit: String = "Ki"
    override val value: Long = 3
  }

  case object MEGA_IOTA extends IotaUnit {
    override val unit: String = "Mi"
    override val value: Long = 6
  }

  case object GIGA_IOTA extends IotaUnit {
    override val unit: String = "Gi"
    override val value: Long = 9
  }

  case object TERA_IOTA extends IotaUnit {
    override val unit: String = "Ti"
    override val value: Long = 12
  }

  case object PETA_IOTA extends IotaUnit {
    override val unit: String = "Pi"
    override val value: Long = 15
  }

}
