package sota.dto.response

import sota.model.Input

case class GetBalancesAndFormatResponse(duration: Long, inputs: Seq[Input], totalBalance2: Long) extends AbstractResponse