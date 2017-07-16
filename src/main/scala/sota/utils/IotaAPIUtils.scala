package sota.utils

import sota.error.InvalidAddressException
import sota.model.{Bundle, Input}
import sota.pow.SCurl

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

object IotaAPIUtils {

  /**
    * Generates a new address
    *
    * @param seed     The tryte-encoded seed. It should be noted that this seed is not transferred.
    * @param security The secuirty level of private key / seed.
    * @param index    The index to start search from. If the index is provided, the generation of the address is not deterministic.
    * @param checksum The adds 9-tryte address checksum
    * @param curl     The curl instance.
    * @return An String with address.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address.
    */
  @throws[InvalidAddressException]
  def newAddress(seed: String, security: Int, index: Int, checksum: Boolean, curl: SCurl): String = {
    val signing = Signing(curl)
    val key = signing.key(Converter.trits(seed), index, security)
    val digests = signing.digests(key)
    val addressTrits = signing.address(digests)

    var address = Converter.trytes(addressTrits)

    if (checksum) {
      address = Checksum.addChecksum(address)
    }

    address
  }

  def signInputsAndReturn(seed: String, inputs: List[Input], bundle: Bundle, signatureFragments: List[String], curl: SCurl): List[String] = {
    bundle.finalize(curl)
    bundle.addTrytes(signatureFragments)

    //  SIGNING OF INPUTS
    //
    //  Here we do the actual signing of the inputs
    //  Iterate over all bundle transactions, find the inputs
    //  Get the corresponding private key and calculate the signatureFragment
    for (i <- bundle.transactions.indices) {
      if (bundle.transactions(i).value < 0) {
        val thisAddress = bundle.transactions(i).address

        // Get the corresponding keyIndex of the address
        var keyIndex = 0
        var keySecurity = 0
        inputs.filter(_.address == thisAddress).foreach { input =>
          keyIndex = input.keyIndex
          keySecurity = input.security
        }

        val bundleHash = bundle.transactions(i).bundle
        val key = Signing(curl).key(Converter.trits(seed), keyIndex, keySecurity)
        val firstFragment = copyOfRange(key, 0, 6561)
        val normalizedBundleHash = bundle.normalizedBundle(bundleHash)
        val firstBundleFragment = copyOfRange(normalizedBundleHash, 0, 27)
        val firstSignedFragment = Signing(curl).signatureFragment(firstBundleFragment, firstFragment)

        bundle.transactions(i).signatureFragments = Converter.trytes(firstSignedFragment)

        for (j <- 1 until keySecurity) {
          for (k <- bundle.transactions.indices) {
            if (bundle.transactions(k).address == thisAddress && bundle.transactions(i).value == 0) {
              val secondFragment = copyOfRange(key, 6561, 6561 * 2)
              val secondBundleFragment = copyOfRange(normalizedBundleHash, 27, 27 * 2)
              val secondSignedFragment = Signing(curl).signatureFragment(secondBundleFragment, secondFragment)
              bundle.transactions(k).signatureFragments = Converter.trytes(secondSignedFragment)
            }
          }
        }
      }
    }

    val bundleTrytes = ArrayBuffer.empty[String]
    bundle.transactions.foreach {
      bundleTrytes += _.toTrytes()
    }

    bundleTrytes.reverse.toList
  }

  def copyOfRange[T: ClassTag](original: Array[T], from: Int, to: Int): Array[T] = {
    val newLength: Int = to - from
    val copy: Array[T] = Array.ofDim[T](newLength)

    Array.copy(original, from, copy, Math.min(original.size, from), newLength)

    copy
  }

}
