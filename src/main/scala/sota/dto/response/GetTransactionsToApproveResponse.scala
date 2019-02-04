package sota.dto.response

case class GetTransactionsToApproveResponse(duration: Long, trunkTransaction: String, branchTransaction: String) extends AbstractResponse