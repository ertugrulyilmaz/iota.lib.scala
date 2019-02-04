package sota.dto.response

case class GetBalancesResponse(duration: Long, balances: Seq[String], milestone: String, milestoneIndex: Int) extends AbstractResponse
