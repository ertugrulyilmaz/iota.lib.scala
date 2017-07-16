package sota.error

/**
  * This exception occurs when a transfer fails because their is not enough balance to perform the transfer.
  */
class NotEnoughBalanceException private(msg: String) extends BaseException(msg) {

  def this() = {
    this("Not enough balance")
  }

}