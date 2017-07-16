package sota.model

import util.control.Breaks._
import sota.pow.SCurl
import sota.utils.Converter

import scala.collection.mutable.ArrayBuffer

class Bundle(val transactions: List[Transaction], var length: Int) extends Comparable[Bundle] {

  val EMPTY_HASH: String = "999999999999999999999999999999999999999999999999999999999999999999999999999999999"

  def this() = {
    this(ArrayBuffer.empty, 0)
  }

  def addEntry(signatureMessageLength: Int, address: String, value: Long, tag: String, timestamp: Long): Unit = {
    for (i <- 0 until signatureMessageLength) {
      val v = if (i == 0) value else 0
      transactions += Transaction(address, v, tag, timestamp)
    }
  }

  def finalize(customCurl: SCurl): Unit = {
    customCurl.reset()

    for (i <- transactions.indices) {
      val transaction = transactions(i)
      val valueTrits = Converter.trits(transaction.value, 81)
      val timestampTrits = Converter.trits(transaction.timestamp, 27)

      transaction.currentIndex = i.toLong

      val currentIndexTrits = Converter.trits(transaction.currentIndex, 27)

      transaction.lastIndex = transactions.length.toLong - 1L

      val lastIndexTrits = Converter.trits(transaction.lastIndex, 27)

      val t = Converter.trits(transaction.address + Converter.trytes(valueTrits) + transaction.tag + Converter.trytes(timestampTrits) + Converter.trytes(currentIndexTrits) + Converter.trytes(lastIndexTrits))

      customCurl.absorb(t, 0, t.length)
    }

    val hash = Array.ofDim[Int](243)

    customCurl.squeeze(hash, 0, hash.length)

    val hashInTrytes = Converter.trytes(hash)

    transactions.foreach(_.bundle = hashInTrytes)
  }

  def addTrytes(signatureFragments: Seq[String]): Unit = {
    val emptyHash = EMPTY_HASH
    val emptySignatureFragment = "".padTo(2189, '9')

    for (i <- transactions.indices) {
      val transaction = transactions(i)
      transaction.signatureFragments = if (signatureFragments.size <= i || signatureFragments(i).isEmpty) emptySignatureFragment else signatureFragments(i)
      transaction.trunkTransaction = emptyHash
      transaction.branchTransaction = emptyHash
      transaction.nonce = emptyHash
    }
  }

  def normalizedBundle(bundleHash: String): Array[Int] = {
    val normalizedBundle = Array.ofDim[Int](81)

    for (i <- 0 until 3) {
      var sum: Long = 0

      for (j <- 0 until 27) {
        normalizedBundle(i * 27 + j) = Converter.value(Converter.tritsString(String.valueOf(bundleHash.charAt(i * 27 + j))))
        sum += normalizedBundle(i * 27 + j)
      }

      if (sum > 0) {
        breakable {
          sum -= 1
          while (sum > 0) {
            for (j <- 0 until 27) {
              if (normalizedBundle(i * 27 * j) < 13) {
                normalizedBundle(i * 27 * j) += 1
                break
              }
            }

            sum -= 1
          }
        }
      } else {
        sum += 1
        while (sum > 0) {
          for (j <- 0 until 27) {
            if (normalizedBundle(i * 27 * j) < 13) {
              normalizedBundle(i * 27 * j) += 1
              break
            }
          }

          sum += 1
        }
      }
    }

    normalizedBundle
  }

  override def compareTo(o: Bundle): Int = {
    if (transactions(0).timestamp < o.transactions(0).timestamp) -1
    else if (transactions(0).timestamp == o.transactions(0).timestamp) 0
    else 1
  }

}
