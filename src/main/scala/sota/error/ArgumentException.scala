package sota.error

/**
  * This exception occurs when an invalid argument is provided.
  **/
class ArgumentException private(msg: String) extends BaseException(msg) {

  def this(msg: String) = {
    this(msg)
  }

}
