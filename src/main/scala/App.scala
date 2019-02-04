import com.typesafe.scalalogging.StrictLogging
import sota.utils.Converter
import sota.protocol.ResponseSerialization

object App extends App with StrictLogging with ResponseSerialization {

  val temp = Array(1, 0, 0, 1, 1, -1, 0, -1, -1, 0, 0, -1, -1, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 1, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 0, -1, 0, 1, 0, 1, -1, 0, 1, 1, 1, 0, 0, -1, -1, -1, 1, 0, 1, -1, 0, 0, 1, 0, -1, 0, 0, -1, 0, 1, 1, -1, 1, -1, -1, -1, 0, -1, 0, 0, 1, 1, -1, -1, -1, 0, 1, 1, 1, -1, 0, 1, 0, -1, -1, -1, 1, -1, -1, -1, 0, -1, 0, 0, 0, -1, 0, 1, -1, 0, 0, -1, -1, 0, 1, -1, 0, -1, 1, 1, 0, -1, 1, -1, -1, 0, 1, -1, 0, -1, -1, 0, -1, 0, 0, 0, 0, -1, -1, -1, 1, 0, 0, 0, 1, 0, 1, -1, 0, -1, -1, 1, 1, 1, -1, 1, 0, 0, 0, 0, 0, -1, -1, -1, 1, 1, -1, -1, -1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, -1, -1, 0, 1, 0, 1, -1, 0, 1, -1, -1, -1, 1, 0, 1, 0, 1, -1, -1, 1, 0, -1, -1, 0, -1, 0, -1, -1, 0, 0, 0, 1, 0, 1, -1, 1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, -1, -1, 0, 1, -1, 0, 1, -1, 1, 1, 1, -1, 1, 0, 0)

  val bytes = Converter.bytes(temp)
  var trits = Array.ofDim[Int](bytes.length)

  logger.info("{}", trits)

  Converter.getTrits(bytes, trits)

  logger.info("{}", trits)

}
