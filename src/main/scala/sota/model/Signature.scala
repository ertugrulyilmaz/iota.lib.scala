package sota.model

case class Signature(address: Option[String] = Option.empty, var signatureFragments: List[String] = List.empty[String])

