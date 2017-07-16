package sota.error

/**
  * This exception occurs when an invalid transfer is provided.
  */
class InvalidTransferException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid Transfer!")
  }

}
