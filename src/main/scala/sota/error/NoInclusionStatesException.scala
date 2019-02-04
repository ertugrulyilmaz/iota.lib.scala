package sota.error

/**
  * This exception occurs when it not possible to get a inclusion state.
  */
class NoInclusionStatesException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("No inclusion states for the provided seed")
  }

}
