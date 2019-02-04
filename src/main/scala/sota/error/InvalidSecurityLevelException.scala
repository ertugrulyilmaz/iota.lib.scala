package sota.error

/**
  * This exception occurs when an invalid security level is provided.
  */
class InvalidSecurityLevelException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid security level")
  }

}
