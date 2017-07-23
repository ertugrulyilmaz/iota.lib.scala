package sota.utils

import sota.model.Bundle
import sota.pow.SCurl

trait Multisig {

  val curl: SCurl
  val signingInstance: Signing

  /**
    * @param seed     tryte-encoded seed. It should be noted that this seed is not transferred
    * @param security security secuirty level of private key / seed
    * @param index
    * @return digest trytes
    **/
  def getDigest(seed: String, security: Int, index: Int): String = {
    val key = signingInstance.key(Converter.trits(seed, 243), index, security)

    Converter.trytes(signingInstance.digests(key))
  }

  /**
    * Initiates the generation of a new multisig address or adds the key digest to an existing one
    *
    * @param digestTrytes
    * @param curlStateTrytes
    * @return digest trytes
    **/
  def addAddressDigest(digestTrytes: String, curlStateTrytes: String): String = {
    val digest = Converter.trits(digestTrytes, digestTrytes.length * 3)

    val curlState = if (!curlStateTrytes.isEmpty) Converter.trits(curlStateTrytes, digestTrytes.length * 3)
    else Array.ofDim[Int](digestTrytes.length * 3)

    Array.copy(curlState, 0, curl.state, 0, curlState.length)

    curl.absorb(digest)

    Converter.trytes(curl.state)
  }

  /**
    * Gets the key value of a seed
    *
    * @param seed tryte-encoded seed. It should be noted that this seed is not transferred
    * @param index
    * @return digest trytes
    **/
  def getKey(seed: String, index: Int, security: Int): String = {
    Converter.trytes(signingInstance.key(Converter.trits(seed, 81 * security), index, security))
  }

  /**
    * Generates a new address
    *
    * @param curlStateTrytes
    * @return address
    **/
  def finalizeAddress(curlStateTrytes: String): String = {
    val curlState = Converter.trits(curlStateTrytes)

    Array.copy(curlState, 0, curl.state, 0, curlState.length)

    val addressTrits = Array.ofDim[Int](243)

    curl.squeeze(addressTrits)

    Converter.trytes(addressTrits)
  }

  /**
    * Validates  a generated multisig address
    *
    * @param multisigAddress
    * @param digests
    * @return boolean
    **/
  def validateAddress(multisigAddress: String, digests: Array[Array[Int]]): Boolean = {
    curl.reset()

    digests.foreach(curl.absorb)

    val addressTrits = Array.ofDim[Int](243)

    Converter.trytes(addressTrits).equals(multisigAddress)
  }

  /**
    * Adds the cosigner signatures to the corresponding bundle transaction
    *
    * @param bundleToSign
    * @param inputAddress
    * @param keyTrytes
    * @return Returns bundle trytes.
    **/
  def addSignature(bundleToSign: Bundle, inputAddress: String, keyTrytes: String): Bundle = {
    // Get the security used for the private key
    // 1 security level = 2187 trytes
    val security = keyTrytes.length / 2187

    val key = Converter.trits(keyTrytes)

    // First get the total number of already signed transactions
    // use that for the bundle hash calculation as well as knowing
    // where to add the signature
    var numSignedTxs = 0

    for (i <- bundleToSign.transactions.indices) {
      if (bundleToSign.transactions(i).address.equals(inputAddress)) {
        if (!InputValidator.isNinesTrytes(bundleToSign.transactions(i).signatureFragments, bundleToSign.transactions(i).signatureFragments.length)) {
          numSignedTxs += 1
        } else {
          val bundleHash = bundleToSign.transactions(i).bundle

          //  First 6561 trits for the firstFragment
          val firstFragment: Array[Int] = IotaAPIUtils.copyOfRange(key, 0, 6561)

          val normalizedBundleFragments: Array[Array[Int]] = Array.ofDim[Int](3, 27)
          val normalizedBundleHash: Array[Int] = bundleToSign.normalizedBundle(bundleHash)

          for (k <- 0 until 3) {
            normalizedBundleFragments(k) = IotaAPIUtils.copyOfRange(normalizedBundleHash, k * 27, (k + 1) * 27)
          }

          val firstBundleFragment = normalizedBundleFragments(numSignedTxs % 3)

          val firstSignedFragment = signingInstance.signatureFragment(firstBundleFragment, firstFragment)

          bundleToSign.transactions(i).signatureFragments = Converter.trytes(firstSignedFragment)

          for (j <- 1 until security) {
            //  Next 6561 trits for the firstFragment
            val nextFragment = IotaAPIUtils.copyOfRange(key, 6561 * j, (j + 1) * 6561)

            //  Use the next 27 trytes
            val nextBundleFragment = normalizedBundleFragments((numSignedTxs + j) % 3)

            //  Calculate the new signatureFragment with the first bundle fragment
            val nextSignedFragment = signingInstance.signatureFragment(nextBundleFragment, nextFragment)

            //  Convert signature to trytes and add new bundle entry at i + j position
            // Assign the signature fragment
            bundleToSign.transactions(i + j).signatureFragments = Converter.trytes(nextSignedFragment)
          }

          return bundleToSign
        }
      }
    }

    bundleToSign
  }

}

object Multisig {

  def apply(): Multisig = apply(SCurl())

  def apply(scurl: SCurl): Multisig = new Multisig {
    override val curl: SCurl = scurl

    curl.reset()

    override val signingInstance: Signing = Signing(curl.clone())
  }

}
