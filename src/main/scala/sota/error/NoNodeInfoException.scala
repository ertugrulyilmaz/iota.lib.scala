package sota.error

/**
  * This exception occurs when its not possible to get node info.
  */
class NoNodeInfoException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Node info could not be retrieved")
  }

}