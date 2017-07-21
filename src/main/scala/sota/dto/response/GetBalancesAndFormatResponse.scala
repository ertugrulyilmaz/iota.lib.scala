package sota.dto.response

import sota.model.Input

case class GetBalancesAndFormatResponse(duration: Long, inputs: Seq[Input], totalBalance: Long) extends AbstractResponse
