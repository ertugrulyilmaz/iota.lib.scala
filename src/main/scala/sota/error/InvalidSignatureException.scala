package sota.error

/**
  * This exception occurs when an invalid signature is encountered.
  */
class InvalidSignatureException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid Signatures!")
  }

}