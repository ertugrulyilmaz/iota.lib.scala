package sota.error

/**
  * This exceptions occurs if an invalid bundle was found or provided.
  */
class InvalidBundleException private(msg: String) extends BaseException(msg) {

}
