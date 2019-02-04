package sota.error

/**
  * This exceptions occurs if an invalid bundle was found or provided.
  */
class InvalidBundleException (msg: String) extends BaseException(msg) {

  def this() = {
    this("Invalid Bundle")
  }

}
