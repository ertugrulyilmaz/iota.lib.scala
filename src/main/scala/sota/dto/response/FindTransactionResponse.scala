package sota.dto.response

import scala.collection.mutable.ArrayBuffer

case class FindTransactionResponse(duration: Long, hashes: ArrayBuffer[String]) extends AbstractResponse