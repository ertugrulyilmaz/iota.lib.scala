package sota.error

import scala.collection.mutable.ArrayBuffer


class BaseException private(ex: Exception) extends Exception(ex) {

  var messages = new ArrayBuffer[String]()

  def this(message:String) = {
    this(new Exception(message))
    messages += message
  }

  def this(message:String, throwable: Throwable) = {
    this(new Exception(message, throwable))

    messages += message
  }

  def this(messages:Seq[String]) = {
    this(new Exception())

    this.messages ++= messages
  }

  def this(messages:Seq[String], cause: Throwable) = {
    this(new Exception(cause))

    this.messages ++= messages
  }

  override def getMessage: String = {
    "[" + messages.mkString(", ") + "]"
  }
}

object BaseException {
  def apply(message:String) = new BaseException(message)
  def apply(message:String, throwable: Throwable) = new BaseException(message, throwable)

  def apply(messages:Seq[String]) = new BaseException(messages)
  def apply(messages:Seq[String], throwable: Throwable) = new BaseException(messages, throwable)
}
