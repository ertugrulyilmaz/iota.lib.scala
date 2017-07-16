package sota.model

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.{ToStringBuilder, ToStringStyle}
import sota.pow.SCurl

trait Transaction {

//  val logger: com.typesafe.scalalogging.Logger

  val customCurl: SCurl

  def hash: String
  var signatureFragments: String
  def address: String
  def value: Long
  def tag: String
  def timestamp: Long
  var currentIndex: Long
  var lastIndex: Long
  var bundle: String
  var trunkTransaction: String
  var branchTransaction: String
  var nonce: String
  def persistence: Boolean

  override def toString: String = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)

  override def equals(obj: scala.Any): Boolean = obj.asInstanceOf[Transaction].hash.equals(hash)

  def toTrytes(): String = {
    /*
    val valueTrits: Array[Int] = Converter.trits(this.getValue, 81)
    val timestampTrits: Array[Int] = Converter.trits(this.getTimestamp, 27)
    val currentIndexTrits: Array[Int] = Converter.trits(this.getCurrentIndex, 27)
    val lastIndexTrits: Array[Int] = Converter.trits(this.getLastIndex, 27)

    return signatureFragments
    + address
    + Converter.trytes(valueTrits)
    + tag
    + Converter.trytes(timestampTrits)
    + Converter.trytes(currentIndexTrits)
    + Converter.trytes(lastIndexTrits)
    + bundle
    + trunkTransaction
    + branchTransaction
    + nonce
     */
    ""
  }

  def transactionObject(trytes: String): Unit = {
    if (StringUtils.isEmpty(trytes)) {
//      logger.warn("Warning: empty trytes in input for transactionObject")
      return
    }

    for (i <- 2279 to 2295) {
      if (trytes.charAt(i) != '9') {
//        logger.warn("Trytes {} does not seem a valid tryte", trytes)
        return
      }
    }

    /*
    val transactionTrits: Array[Int] = Converter.trits(trytes)
    val hash: Array[Int] = new Array[Int](243)

    val curl: ICurl = if (customCurl == null) new JCurl
                      else customCurl// we need a fluent JCurl.

    // generate the correct transaction hash
    curl.reset
    curl.absorb(transactionTrits, 0, transactionTrits.length)
    curl.squeeze(hash, 0, hash.length)

    hash = Converter.trytes(hash)
    signatureFragments = trytes.substring(0, 2187))
    address = trytes.substring(2187, 2268)
    value = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837))
    tag = trytes.substring(2295, 2322)
    timestamp = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993))
    currentIndex = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020))
    lastIndex = Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047))
    bundle = trytes.substring(2349, 2430)
    trunkTransaction = trytes.substring(2430, 2511)
    branchTransaction = trytes.substring(2511, 2592)
    nonce = trytes.substring(2592, 2673)
     */
  }

}

object Transaction {

  def apply(trytes: String, _customCurl: SCurl): Transaction = new Transaction {
    transactionObject(trytes)

    override val customCurl = _customCurl
    override def hash: String = ""
    override var signatureFragments: String = ""
    override def address: String = ""
    override def value: Long = 0L
    override def tag: String = ""
    override def timestamp: Long = 0L
    override var currentIndex: Long = 0
    override var lastIndex: Long = 0
    override var bundle: String = ""
    override var trunkTransaction: String = ""
    override var branchTransaction: String = ""
    override var nonce: String = ""
    override def persistence: Boolean = false
  }
  
  def apply(_address: String, _value: Long, _tag: String, _timestamp: Long) = new Transaction {
    override val customCurl = SCurl()
    override def hash: String = ""
    override var signatureFragments: String = ""
    override def address: String = _address
    override def value: Long = _value
    override def tag: String = _tag
    override def timestamp: Long = _timestamp
    override var currentIndex: Long = 0
    override var lastIndex: Long = 0
    override var bundle: String = ""
    override var trunkTransaction: String = ""
    override var branchTransaction: String = ""
    override var nonce: String = ""
    override def persistence: Boolean = false
  }

}
