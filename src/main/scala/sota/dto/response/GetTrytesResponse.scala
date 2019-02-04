package sota.dto.response

case class GetTrytesResponse(duration: Long, trytes: Array[String]) extends AbstractResponse