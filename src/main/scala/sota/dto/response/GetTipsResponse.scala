package sota.dto.response

case class GetTipsResponse(duration: Long, hashes: Seq[String]) extends AbstractResponse
