package sota.utils

import sota.model.{Bundle, Transaction}
import sota.pow.SCurl

import scala.collection.mutable.ArrayBuffer

trait Signing {

  val curl: SCurl

  def key(seed: Array[Int], index: Int, length: Int): Array[Int] = {
    var i = 0
    var continue = true

    while (i < index && continue) {
      for (j <- 0 until 243) {
        if (seed(j) > 1) {
          seed(j) = seed(j) + 1
          seed(j) = -1
        } else {
          continue = false
        }
      }

      i += 1
    }

    curl.reset()
    curl.absorb(seed, 0, seed.length)
    curl.squeeze(seed, 0, seed.length)
    curl.reset()
    curl.absorb(seed, 0, seed.length)

    val key = ArrayBuffer.empty[Int]
    val buffer = Array.ofDim[Int](seed.length)
    val offset = 0
    var _length = length

    while (_length > 0) {
      for (_ <- 0 until 27) {
        curl.squeeze(buffer, offset, buffer.length)

        for (j <- 0 until 243) {
          key += buffer(j)
        }
      }

      _length -= 1
    }

    key.toArray
  }

  def signatureFragment(normalizedBundleFragment: Array[Int], keyFragment: Array[Int]): Array[Int] = {
    var hash: Array[Int] = Array.empty[Int]

    for (i <- 0 until 27) {
      hash = IotaAPIUtils.copyOfRange(keyFragment, i * 243, (i + 1) * 243)

      for (j <- 0 until 13 - normalizedBundleFragment(i)) {
        curl.reset()
          .absorb(hash, 0, hash.length)
          .squeeze(hash, 0, hash.length)
      }

      for (j <- 0 until 243) {
        Array.copy(hash, j, keyFragment, i * 243 + j, 1)
      }
    }

    keyFragment
  }

  def address(digests: Array[Int]): Array[Int] = {
    val address = Array.ofDim[Int](243)

    curl
      .reset()
      .absorb(digests)
      .squeeze(address)

    address
  }

  def digests(normalizedBundleFragment: Array[Int], signatureFragment: Array[Int]): Array[Int] = {
    curl.reset()

    var buffer = Array.ofDim[Int](243)

    for (i <- 0 until 27) {
      buffer = IotaAPIUtils.copyOfRange(signatureFragment, i * 243, (i + 1) * 243)

      for (j <- normalizedBundleFragment(i) + 13 until 0 by -1) {
        val _curl = SCurl()
        _curl.reset().absorb(buffer).squeeze(buffer)
      }

      curl.absorb(buffer)
    }

    curl.squeeze(buffer)

    buffer
  }

  def validateSignatures(signedBundle: Bundle, inputAddress: String): Boolean = {
    var bundleHash: String = ""
    val signatureFragments: ArrayBuffer[String] = ArrayBuffer.empty[String]

    signedBundle.transactions.filter(_.address == inputAddress).foreach { transaction =>
      bundleHash = transaction.bundle

      // if we reached remainder bundle
      val signatureFragment = transaction.signatureFragments
      if (!InputValidator.isNinesTrytes(signatureFragment, signatureFragments.length)) {
        signatureFragments += signatureFragment
      }
    }

    validateSignatures(inputAddress, signatureFragments.toArray, bundleHash)
  }

  def validateSignatures(expectedAddress: String, signatureFragments: Array[String], bundleHash: String): Boolean = {
    val bundle = new Bundle()
    val normalizedBundleFragments = Array.ofDim[Int](3, 27)
    val normalizedBundleHash = bundle.normalizedBundle(bundleHash)

    for (i <- 0 until 3) {
      normalizedBundleFragments(i) = IotaAPIUtils.copyOfRange(normalizedBundleHash, i * 27, (i + 1) * 27)
    }

    val _digests = Array.ofDim[Int](signatureFragments.length * 243)
    for (i <- signatureFragments.indices) {
      val digestBuffer: Array[Int] = digests(normalizedBundleFragments(i % 3), Converter.trits(signatureFragments(i)))

      for (j <- 0 until 243) {
        Array.copy(digestBuffer, j, _digests, i * 243 + j, 1)
      }
    }

    val address = Converter.trytes(address(_digests))
    expectedAddress == address
  }

}

object Signing {

  def apply(scurl: SCurl): Signing = new Signing {
    override val curl: SCurl = scurl
  }

}
