package sota.dto.response

import sota.model.Neighbor

case class GetNeighborsResponse(duration: Long, neighbors: List[Neighbor]) extends AbstractResponse