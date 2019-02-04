package sota.dto.response

case class FindTransactionResponse(duration: Long, hashes: Seq[String]) extends AbstractResponse
