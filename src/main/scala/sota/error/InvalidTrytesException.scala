package sota.error

/**
  * This exception occurs when invalid trytes is provided.
  */
class InvalidTrytesException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid input trytes")
  }

}
