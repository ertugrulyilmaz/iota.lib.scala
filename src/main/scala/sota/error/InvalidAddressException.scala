package sota.error

/**
  * This exception occurs when an invalid address is provided.
  */
class InvalidAddressException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid Address")
  }

}