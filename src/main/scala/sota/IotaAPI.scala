package sota

import java.util.Calendar

import com.typesafe.scalalogging.StrictLogging
import org.apache.commons.lang3.StringUtils
import sota.dto.response._
import sota.error.{ArgumentException, InvalidTrytesException, _}
import sota.model._
import sota.pow.SCurl
import sota.utils._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

trait IotaAPI extends IotaAPICore with StrictLogging {

  /**
    * Generates a new address from a seed and returns the remainderAddress.
    * This is either done deterministically, or by providing the index of the new remainderAddress.
    *
    * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
    * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
    * @param index     Key index to start search from. If the index is provided, the generation of the address is not deterministic.
    * @param checksum  Adds 9-tryte address checksum.
    * @param total     Total number of addresses to generate.
    * @param returnAll If <code>true</code>, it returns all addresses which were deterministically generated (until findTransactions returns null).
    * @return An array of strings with the specifed number of addresses.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    */
  @throws[InvalidAddressException]
  @throws[InvalidSecurityLevelException]
  def getNewAddress(seed: String, security: Int, index: Int, checksum: Boolean, total: Int, returnAll: Boolean): GetNewAddressResponse = security match {
    case _ if security < 1 || security > 3 => throw new InvalidSecurityLevelException()
    case _ if total != 0 =>
      val stopWatch = StopWatch()
      val allAddresses = ArrayBuffer.empty[String]

      for (i <- index until index + total) {
        allAddresses += IotaAPIUtils.newAddress(seed, security, i, checksum, customCurl.clone())
      }

      GetNewAddressResponse(stopWatch.getElapsedTimeMili(), allAddresses.toList)
    case _ =>
      val stopWatch = StopWatch()
      var allAddresses = ArrayBuffer.empty[String]

      var i = index
      var continue = true

      while (continue) {
        val newAddress = IotaAPIUtils.newAddress(seed, security, i, checksum, customCurl.clone())
        val response = findTransactionsByAddresses(newAddress)

        allAddresses += newAddress

        response match {
          case Some(r) if r.hashes.isEmpty => continue = false
          case _ => i += 1
        }
      }

      if (!returnAll) {
        allAddresses = allAddresses.slice(allAddresses.size - 1, allAddresses.size)
      }

      GetNewAddressResponse(stopWatch.getElapsedTimeMili(), allAddresses.toList)
  }

  /**
    * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
    * @param security        The security level of private key / seed.
    * @param start           Starting key index.
    * @param end             Ending key index.
    * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
    * @return Bundle of transfers.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws ArgumentException             is thrown when an invalid argument is provided.
    * @throws InvalidBundleException        is thrown if an invalid bundle was found or provided.
    * @throws InvalidSignatureException     is thrown when an invalid signature is encountered.
    * @throws NoNodeInfoException           is thrown when its not possible to get node info.
    * @throws NoInclusionStatesException    when it not possible to get a inclusion state.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    */
  @throws[ArgumentException]
  @throws[InvalidBundleException]
  @throws[InvalidSignatureException]
  @throws[NoNodeInfoException]
  @throws[NoInclusionStatesException]
  @throws[InvalidSecurityLevelException]
  @throws[InvalidAddressException]
  def getTransfers(seed: String, security: Int, start: Int, end: Int, inclusionStates: Boolean): GetTransferResponse = {
    val stopWatch = StopWatch()
    val _seed = InputValidator.validateSeed(seed)

    if (_seed == null) {
      throw new IllegalStateException("Invalid Seed")
    }

    if (security < 1 || security > 3) {
      throw new InvalidSecurityLevelException()
    }

    val _start = if (start != null) 0 else start

    if (_start > end || end > (_start + 500)) {
      throw new ArgumentException()
    }

    val gnr = getNewAddress(seed, security, start, false, end, true)

    if (gnr != null && gnr.addresses != null) {
      val bundles = bundlesFromAddresses(gnr.addresses.toArray, inclusionStates)

      GetTransferResponse(stopWatch.getElapsedTimeMili(), bundles)
    }

    GetTransferResponse(stopWatch.getElapsedTimeMili(), Array.empty[Bundle])
  }

  /**
    * Internal function to get the formatted bundles of a list of addresses.
    *
    * @param addresses       List of addresses.
    * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
    * @return A Transaction objects.
    * @throws ArgumentException          is thrown when an invalid argument is provided.
    * @throws InvalidBundleException     is thrown if an invalid bundle was found or provided.
    * @throws InvalidSignatureException  is thrown when an invalid signature is encountered.
    * @throws NoNodeInfoException        is thrown when its not possible to get node info.
    * @throws NoInclusionStatesException when it not possible to get a inclusion state.
    */
  @throws[ArgumentException]
  @throws[InvalidBundleException]
  @throws[InvalidSignatureException]
  @throws[NoNodeInfoException]
  @throws[NoInclusionStatesException]
  def bundlesFromAddresses(addresses: Array[String], inclusionStates: Boolean): Array[Bundle] = {
    //    val trxs: List[Transaction] = findTransactionObjects(addresses)
    //    val tailTransactions = ArrayBuffer.empty[String]
    //    val nonTailBundleHashes = ArrayBuffer.empty[String]
    //
    //    trxs.foreach {
    //      case trx if trx.currentIndex == 0 =>
    //        tailTransactions += trx.hash
    //      case trx if nonTailBundleHashes.indexOf(trx.bundle) == -1 =>
    //        nonTailBundleHashes += trx.bundle
    //    }
    //
    //    val bundleObjects: List[Transaction] = findTransactionObjectsByBundle(nonTailBundleHashes)
    //
    //    bundleObjects.foreach {
    //      case trx if trx.currentIndex == 0 && tailTransactions.indexOf(trx.hash) == -1 =>
    //        tailTransactions += trx.hash
    //    }
    //
    //    val finalBundles: List[Bundle] = List.empty[Bundle]
    //
    //    val tailTxArray = tailTransactions.toArray
    Array.empty[Bundle]
  }

  /**
    * Wrapper function that broadcasts and stores the specified trytes.
    *
    * @param trytes The trytes.
    * @return A StoreTransactionsResponse.
    * @throws BroadcastAndStoreException is thrown if its not possible to broadcast and store.
    */
  @throws[BroadcastAndStoreException]
  def broadcastAndStore(trytes: Array[String]): StoreTransactionsResponse = {
    try {
      broadcastTransactions(trytes)
    } catch {
      case e: Exception =>
        logger.error("Impossible to broadcastAndStore, aborting.", e);
        throw new BroadcastAndStoreException()
    }

    storeTransactions(trytes)
  }

  /**
    * Facade method: Gets transactions to approve, attaches to Tangle, broadcasts and stores.
    *
    * @param trytes             The trytes.
    * @param depth              The depth.
    * @param minWeightMagnitude The minimum weight magnitude.
    * @return Transactions objects.
    * @throws InvalidTrytesException is thrown when invalid trytes is provided.
    */
  @throws[InvalidTrytesException]
  def sendTrytes(trytes: Array[String], depth: Int, minWeightMagnitude: Int): Seq[Transaction] = {
    val txs: GetTransactionsToApproveResponse = getTransactionsToApprove(depth)
    val res: GetAttachToTangleResponse = attachToTangle(txs.trunkTransaction, txs.branchTransaction, minWeightMagnitude, trytes: _*)

    try {
      broadcastAndStore(res.trytes)

      res.trytes.map(Transaction(_, customCurl.clone()))
    } catch {
      case _: BroadcastAndStoreException => Seq.empty[Transaction]
    }
  }

  /**
    * Wrapper function for getTrytes and transactionObjects.
    * Gets the trytes and transaction object from a list of transaction hashes.
    *
    * @param hashes The hashes
    * @return Transaction objects.
    **/
  def getTransactionsObjects(hashes: Array[String]): Seq[Transaction] = {
    if (!InputValidator.isArrayOfHashes(hashes)) {
      throw new IllegalStateException("Not an Array of Hashes: " + hashes)
    }

    getTrytes(hashes: _*).trytes.map(Transaction(_, customCurl.clone()))
  }

  /**
    * Wrapper function for findTransactions, getTrytes and transactionObjects.
    * Returns the transactionObject of a transaction hash. The input can be a valid findTransactions input.
    *
    * @param input The inputs.
    * @return Transactions.
    **/
  def findTransactionObjects(input: Array[String]): Seq[Transaction] = {
    val ftr: Option[FindTransactionResponse] = findTransactions(input, null, null, null)

    ftr.map { f =>
      getTransactionsObjects(f.hashes.toArray)
    }.getOrElse(Seq.empty[Transaction])
  }

  /**
    * Wrapper function for findTransactions, getTrytes and transactionObjects.
    * Returns the transactionObject of a transaction hash. The input can be a valid.
    * findTransactions input
    *
    * @param input The inputs.
    * @return Transactions.
    **/
  def findTransactionObjectsByBundle(input: String*): Seq[Transaction] = {
    val ftr: Option[FindTransactionResponse] = findTransactions(null, null, null, Array(input: _*))

    ftr.map { f =>
      getTransactionsObjects(f.hashes.toArray)
    }.getOrElse(Seq.empty[Transaction])
  }

  /**
    * Prepares transfer by generating bundle, finding and signing inputs.
    *
    * @param seed      81-tryte encoded address of recipient.
    * @param security  The security level of private key / seed.
    * @param transfers Array of transfer objects.
    * @param remainder If defined, this address will be used for sending the remainder value (of the inputs) to.
    * @param inputs    The inputs.
    * @return Returns bundle trytes.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws NotEnoughBalanceException     is thrown when a transfer fails because their is not enough balance to perform the transfer.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    * @throws InvalidTransferException      is thrown when an invalid transfer is provided.
    */
  @throws[NotEnoughBalanceException]
  @throws[InvalidSecurityLevelException]
  @throws[InvalidAddressException]
  @throws[InvalidTransferException]
  def prepareTransfers(seed: String, security: Int, transfers: Array[Transfer], remainder: String, inputs: Seq[Input]): Seq[String] = {
    // Input validation of transfers object
    if (!InputValidator.isTransfersCollectionValid(transfers)) {
      throw new InvalidTransferException()
    }

    // validate & if needed pad seed
    val _seed = InputValidator.validateSeed(seed)
    if (_seed.isEmpty) {
      throw new IllegalStateException("Invalid Seed")
    }

    if (security < 1 || security > 3) {
      throw new InvalidSecurityLevelException()
    }

    val bundle = new Bundle()
    val signatureFragments = ArrayBuffer.empty[String]
    var totalValue = 0L
    var tag = ""

    transfers.foreach { transfer =>
      // If address with checksum then remove checksum
      if (Checksum.isAddressWithChecksum(transfer.address)) {
        transfer.address = Checksum.removeChecksum(transfer.address)
      }

      var signatureMessageLength = 1

      // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
      if (transfer.message.length > 2187) {
        // Get total length, message / maxLength (2187 trytes)
        signatureMessageLength += Math.floor(transfer.message.length() / 2187)

        var msgCopy = transfer.message

        while (!msgCopy.isEmpty) {
          var fragment = StringUtils.substring(msgCopy, 0, 2187)
          msgCopy = StringUtils.substring(msgCopy, 2187, msgCopy.length)

          // Pad remainder of fragment
          fragment = StringUtils.rightPad(fragment, 2187, '9')
          signatureFragments += fragment
        }
      } else {
        // Else, get single fragment with 2187 of 9's trytes
        var fragment = StringUtils.substring(transfer.message, 0, 2187)
        fragment = StringUtils.rightPad(fragment, 2187, '9')
        signatureFragments += fragment
      }

      // get current timestamp in seconds
      val timestamp: Long = Math.floor(Calendar.getInstance().getTimeInMillis / 1000).toLong

      // If no tag defined, get 27 tryte tag.
      tag = if (transfer.tag.isEmpty) "999999999999999999999999999" else transfer.tag

      // Pad for required 27 tryte length
      tag = StringUtils.rightPad(tag, 27, '9')

      // Add first entry to the bundle
      bundle.addEntry(signatureMessageLength, transfer.address, transfer.value, tag, timestamp)

      // Sum up total value
      totalValue += transfer.value
    }

    // Get inputs if we are sending tokens
    if (totalValue != 0) {
      //  Case 1: user provided inputs
      //  Validate the inputs by calling getBalances
      if (inputs.nonEmpty) {
        // Get list if addresses of the provided inputs
        val inputsAddresses = inputs.map(_.address).toArray
        val balances = getBalances(100, inputsAddresses).balances
        val confirmedInputs = ArrayBuffer.empty[Input]
        var totalBalance = 0
        var i = 0

        balances.filter(_.toInt > 0).foreach { balance =>
          // if we've already reached the intended input value, break out of loop
          if (totalBalance < totalValue) {
            totalBalance += balance.toInt
            val inputEl = inputs(i).copy(balance = balance.toInt)
            confirmedInputs += inputEl
          }

          i += 1
        }

        // Return not enough balance error
        if (totalValue > totalBalance) {
          throw new IllegalStateException("Not enough balance")
        }

        addRemainder(seed, security, confirmedInputs, bundle, tag, totalValue, null, signatureFragments)
      } else {
        //  Case 2: Get inputs deterministically
        //
        //  If no inputs provided, derive the addresses from the seed and
        //  confirm that the inputs exceed the threshold

        addRemainder(seed, security, newinputs.getInput(), bundle, tag, totalValue, null, signatureFragments)
      }
    } else {
      // If no input required, don't sign and simply finalize the bundle
      bundle.finalize(customCurl.clone())
      bundle.addTrytes(signatureFragments)

      bundle.transactions.map(_.toTrytes()).reverse
    }
  }

  /**
    * Gets the inputs of a seed
    *
    * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
    * @param security  The Security level of private key / seed.
    * @param start     Starting key index.
    * @param end       Ending key index.
    * @param threshold Min balance required.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    **/
  @throws[InvalidAddressException]
  @throws[InvalidSecurityLevelException]
  def getInputs(seed: String, security: Int, start: Int, end: Int, threshold: Long): GetBalancesAndFormatResponse = {
    // validate the seed
    if (!InputValidator.isTrytes(seed, 0)) {
      throw new IllegalStateException("Invalid Seed");
    }

    val _seed = InputValidator.validateSeed(seed)

    if (_seed.isEmpty) {
      throw new IllegalStateException("Invalid Seed")
    }

    if (security < 1 || security > 3) {
      throw new InvalidSecurityLevelException();
    }

    // If start value bigger than end, return error
    // or if difference between end and start is bigger than 500 keys
    if (start > end || end > (start + 500)) {
      throw new IllegalStateException("Invalid inputs provided")
    }

    //  Case 1: start and end
    //
    //  If start and end is defined by the user, simply iterate through the keys
    //  and call getBalances
    if (end != 0) {
      val allAddresses = ArrayBuffer.empty[String]
      for (i <- start until end) {
        allAddresses += IotaAPIUtils.newAddress(seed, security, i, false, customCurl.clone())
      }

      getBalanceAndFormat(allAddresses.toList, threshold, start, end, StopWatch(), security)
    } else {
      //  Case 2: iterate till threshold || end
      //
      //  Either start from index: 0 or start (if defined) until threshold is reached.
      //  Calls getNewAddress and deterministically generates and returns all addresses
      //  We then do getBalance, format the output and return it

      val addresses = getNewAddress(seed, security, start, false, 0, true).addresses

      getBalanceAndFormat(addresses, threshold, start, end, StopWatch(), security)
    }
  }

  /**
    * Gets the balances and formats the output.
    *
    * @param addresses The addresses.
    * @param threshold Min balance required.
    * @param start     Starting key index.
    * @param end       Ending key index.
    * @param stopWatch the stopwatch.
    * @param security  The security level of private key / seed.
    * @return Inputs object.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    **/
  @throws[InvalidSecurityLevelException]
  def getBalanceAndFormat(addresses: List[String], threshold: Long, start: Int, end: Int, stopWatch: StopWatch, security: Int): GetBalancesAndFormatResponse = {
    if (security < 1 || security > 3) {
      throw new InvalidSecurityLevelException()
    }

    val getBalancesResponse: GetBalancesResponse = getBalances(100, addresses)
    val balances = getBalancesResponse.balances.toList

    // If threshold defined, keep track of whether reached or not
    // else set default to true
    var thresholdReached: Boolean = threshold == 0
    var i = 0
    val inputs = ArrayBuffer.empty[Input]
    var totalBalance = 0L

    addresses.foreach { address =>
      val balance = balances(i).toLong

      if (balance > 0 && !thresholdReached) {
        val newEntry = Input(address, balance, start + i, security)
        inputs += newEntry

        // Increase totalBalance of all aggregated inputs
        totalBalance += balance

        if (totalBalance >= threshold) {
          thresholdReached = true
        }

        i += 1
      }
    }

    if (thresholdReached) {
      return GetBalancesAndFormatResponse(stopWatch.getElapsedTimeMili(), inputs, totalBalance)
    }

    throw new IllegalStateException("Not enough balance")
  }

  /**
    * Gets the associated bundle transactions of a single transaction.
    * Does validation of signatures, total sum as well as bundle order.
    *
    * @param transaction The transaction encoded in trytes.
    * @return an array of bundle, if there are multiple arrays it means that there are conflicting bundles.
    * @throws ArgumentException         is thrown when an invalid argument is provided.
    * @throws InvalidBundleException    is thrown if an invalid bundle was found or provided.
    * @throws InvalidSignatureException is thrown when an invalid signature is encountered.
    */
  @throws[ArgumentException]
  @throws[InvalidBundleException]
  @throws[InvalidSignatureException]
  def getBundle(transaction: String): GetBundleResponse = {
    val stopWatch = StopWatch()
    val bundle: Bundle = traverseBundle(transaction, null, new Bundle())

    if (bundle == null) {
      throw new ArgumentException("Unknown Bundle")
    }

    var totalSum = 0L
    val bundleHash = bundle.transactions(0).bundle
    val curl = SCurl()
    curl.reset()

    var signaturesToValidate = ArrayBuffer.empty[Signature]
    val signature = Signature()

    for (i <- bundle.transactions.indices) {
      val trx = bundle.transactions(i)
      val bundleValue = trx.value

      totalSum += bundleValue

      if (i != bundle.transactions(i).currentIndex) {
        throw new ArgumentException("Invalid Bundle")
      }

      val trxTrytes = trx.toTrytes().substring(2187, 2187 + 162)

      // Absorb bundle hash + value + timestamp + lastIndex + currentIndex trytes.
      curl.absorb(Converter.trits(trxTrytes))

      // Check if input transaction
      if (bundleValue < 0) {
        val sig = signature.copy(Some(trx.address), signature.signatureFragments ++ List(trx.signatureFragments))

        // Find the subsequent txs with the remaining signature fragment
        for (j <- i until bundle.transactions.size) {
          val newBundleTx = bundle.transactions(i + 1)

          // Check if new tx is part of the signature fragment
          if (newBundleTx.address == trx.address && newBundleTx.value == 0) {
            if (sig.signatureFragments.indexOf(newBundleTx.signatureFragments) == -1) {
              sig.signatureFragments += newBundleTx.signatureFragments
            }
          }
        }

        signaturesToValidate ++= Seq(sig)
      }
    }

    // Check for total sum, if not equal 0 return error
    if (totalSum != 0) throw new InvalidBundleException("Invalid Bundle Sum")

    val bundleFromTrxs = Array.ofDim[Int](243)
    curl.squeeze(bundleFromTrxs)

    val bundleFromTxString = Converter.trytes(bundleFromTrxs)

    // Check if bundle hash is the same as returned by tx object
    if (bundleFromTxString != bundleHash) throw new InvalidBundleException("Invalid Bundle Hash")

    // Last tx in the bundle should have currentIndex === lastIndex
    bundle.length = bundle.transactions.length

    if (bundle.transactions(bundle.length - 1).currentIndex != (bundle.transactions(bundle.length - 1).lastIndex)) {
      throw new InvalidBundleException("Invalid Bundle")
    }

    signaturesToValidate.foreach { stv =>
      val signatureFragments = stv.signatureFragments.toArray
      val address = stv.address.getOrElse("")
      val isValidSignature = Signing(customCurl.clone()).validateSignatures(address, signatureFragments, bundleHash)

      if (!isValidSignature) throw new InvalidSignatureException()
    }

    GetBundleResponse(stopWatch.getElapsedTimeMili(), bundle.transactions.toList)
  }

  /**
    * Replays a transfer by doing Proof of Work again.
    *
    * @param transaction        The transaction.
    * @param depth              The depth.
    * @param minWeightMagnitude The minimum weight magnitude.
    * @return Analyzed Transaction objects.
    * @throws InvalidBundleException    is thrown if an invalid bundle was found or provided.
    * @throws ArgumentException         is thrown when an invalid argument is provided.
    * @throws InvalidSignatureException is thrown when an invalid signature is encountered.
    * @throws InvalidTransferException  is thrown when an invalid transfer is provided.
    */
  @throws[InvalidBundleException]
  @throws[ArgumentException]
  @throws[InvalidSignatureException]
  @throws[InvalidTransferException]
  def replayBundle(transaction: String, depth: Int, minWeightMagnitude: Int): ReplayBundleResponse = {
    val stopWatch = StopWatch()
    val bundleTrytes = ArrayBuffer.empty[String]
    val bundleResponse = getBundle(transaction)
    val bundle = new Bundle(bundleResponse.transactions, bundleResponse.transactions.length)
    bundle.transactions.foreach { trx =>
      bundleTrytes ++= Seq(trx.toTrytes())
    }

    val trxs = sendTrytes(bundleTrytes.toArray, depth, minWeightMagnitude)
    val successful = Array.ofDim[Boolean](trxs.length)

    for (i <- trxs.indices) {
      val response: Option[FindTransactionResponse] = findTransactionsByBundles(trxs(i).bundle)
      successful(i) = response.getOrElse(FindTransactionResponse(0, ArrayBuffer.empty[String])).hashes.nonEmpty
    }

    ReplayBundleResponse(stopWatch.getElapsedTimeMili(), successful)
  }

  /**
    * Wrapper function for getNodeInfo and getInclusionStates
    *
    * @param hashes The hashes.
    * @return Inclusion state.
    * @throws NoNodeInfoException is thrown when its not possible to get node info.
    */
  @throws[NoNodeInfoException]
  def getLatestInclusion(hashes: Array[String]): GetInclusionStateResponse = {
    val getNodeInfoResponse = getNodeInfo()

    val response = Await.result(getNodeInfoResponse, Duration.Inf)

    getInclusionStates(hashes, Array(response.latestSolidSubtangleMilestone))
  }

  /**
    * Wrapper function that basically does prepareTransfers, as well as attachToTangle and finally, it broadcasts and stores the transactions locally.
    *
    * @param seed               Tryte-encoded seed
    * @param security           The security level of private key / seed.
    * @param depth              The depth.
    * @param minWeightMagnitude The minimum weight magnitude.
    * @param transfers          Array of transfer objects.
    * @param inputs             List of inputs used for funding the transfer.
    * @param address            If defined, this address will be used for sending the remainder value (of the inputs) to.
    * @return Array of Transaction objects.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws NotEnoughBalanceException     is thrown when a transfer fails because their is not enough balance to perform the transfer.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    * @throws InvalidTrytesException        is thrown when invalid trytes is provided.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    * @throws InvalidTransferException      is thrown when an invalid transfer is provided.
    */
  @throws[InvalidAddressException]
  @throws[NotEnoughBalanceException]
  @throws[InvalidSecurityLevelException]
  @throws[InvalidTrytesException]
  @throws[InvalidAddressException]
  @throws[InvalidTransferException]
  def sendTransfer(seed: String, security: Int, depth: Int, minWeightMagnitude: Int, transfers: Array[Transfer], inputs: Array[Input], address: String): SendTransferResponse = {
    if (security < 1 || security > 3) {
      throw new InvalidSecurityLevelException()
    }

    val stopWatch = new StopWatch()

    val trytes = prepareTransfers(seed, security, transfers, address, inputs)
    val trxs = sendTrytes(trytes.toArray, depth, minWeightMagnitude)

    val successful = trxs.map { trx =>
      val response = findTransactionsByBundles(trx.bundle)
      response.getOrElse(FindTransactionResponse(0L, ArrayBuffer.empty[String])).hashes.isEmpty
    }

    SendTransferResponse(stopWatch.getElapsedTimeMili(), successful.toArray)
  }

  /**
    * Basically traverse the Bundle by going down the trunkTransactions until
    * the bundle hash of the transaction is no longer the same. In case the input
    * transaction hash is not a tail, we return an error.
    *
    * @param trunkTx    Hash of a trunk or a tail transaction of a bundle.
    * @param bundleHash The bundle hashes.
    * @param bundle     List of bundles to be populated.
    * @return Transaction objects.
    * @throws ArgumentException is thrown when an invalid argument is provided.
    */
  @throws[ArgumentException]
  def traverseBundle(trunkTx: String, bundleHash: Option[String], bundle: Bundle): Bundle = {
    getTrytes(trunkTx) match {
      case Some(gtr) if gtr.trytes.isEmpty => throw new ArgumentException("Bundle transactions not visible")
      case Some(gtr) =>
        val trx = Transaction(gtr.trytes(0), customCurl.clone())

        if (trx.bundle.isEmpty) {
          throw new ArgumentException("Invalid trytes, could not create object")
        }

        // If first transaction to search is not a tail, return error
        if (bundleHash.isEmpty && trx.currentIndex != 0) {
          throw new ArgumentException("Invalid tail transaction supplied.")
        }

        // If different bundle hash, return with bundle
        if (bundleHash != Some(trx.bundle)) {
          return bundle
        }

        // If only one bundle element, return
        if (trx.lastIndex == 0 && trx.currentIndex == 0) {
          return new Bundle(List(trx), 1)
        }

        val trxs = bundle.transactions ++ List(trx)

        traverseBundle(trunkTx, bundleHash, bundle)
      case None => throw new ArgumentException("Get Trytes Response was null")
    }
  }

  /**
    * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
    * Does not contain signatures.
    *
    * @param securitySum      The sum of security levels used by all co-signers.
    * @param inputAddress     Array of input addresses as well as the securitySum.
    * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
    * @return Bundle of transaction objects.
    * @throws InvalidBundleException  is thrown if an invalid bundle was found or provided.
    * @throws InvalidAddressException is thrown when the specified address is not an valid address.
    */
  @throws[InvalidBundleException]
  @throws[InvalidAddressException]
  def initiateTransfer(securitySum: Int, inputAddress: String, remainderAddress: String, transfers: Seq[Transfer], testMode: Boolean): Seq[Transaction] = {
    val sw = new StopWatch()

    // If message or tag is not supplied, provide it
    // Also remove the checksum of the address if it's there
    val _transfers = transfers.map { transfer =>
      val message = if (transfer.message.isEmpty) transfer.message.padTo(2187, "9").mkString("")
      else transfer.message

      val tag = if (transfer.tag.isEmpty) transfer.tag.padTo(2187, "9").mkString("")
      else transfer.tag

      val address = if (Checksum.isValidChecksum(transfer.address)) Checksum.removeChecksum(transfer.address)
      else transfer.address

      transfer.copy(message = message, tag = tag, address = address)
    }

    // Input validation of transfers object
    if (!InputValidator.isTransfersCollectionValid(_transfers)) throw new InvalidTransferException()

    // validate input address
    if (!InputValidator.isAddress(inputAddress)) throw new InvalidAddressException()

    // validate remainder address
    if (!InputValidator.isAddress(remainderAddress)) throw new InvalidBundleException()

    // Create a new bundle
    val bundle = new Bundle()
    var tag = ""

    //  Iterate over all transfers, get totalValue
    //  and prepare the signatureFragments, message and tag
    val data = transfers.map { transfer =>
      var signatureMessageLength = 1

      // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
      val fragments = if (transfer.message.length() > 2187) {
        // Get total length, message / maxLength (2187 trytes)
        signatureMessageLength += Math.floor(transfer.message.length / 2187)

        messageToFragments(transfer.message, Seq.empty[String])
      } else {
        // Else, get single fragment with 2187 of 9's trytes
        Seq(transfer.message.substring(0, 2187).padTo(2187, "9").mkString(""))
      }

      // get current timestamp in seconds
      val timestamp = Math.floor(Calendar.getInstance().getTimeInMillis / 1000)
      tag = transfer.tag.padTo(27, "9").mkString("")

      // Add first entry to the bundle
      bundle.addEntry(signatureMessageLength, transfer.address, transfer.value, tag, timestamp.toLong)

      (transfer.value, fragments)
    }.foldLeft((1L, Seq.empty[String]))((c, acc) => (c._1 + acc._1, acc._2 ++ c._2))

    if (data._1 != 0) {
      val balancesResponse = getBalances(100, Array(inputAddress))
      val balances = balancesResponse.balances

      var totalBalance = balances.map(_.toLong).sum
      val timestamp = Math.floor(Calendar.getInstance().getTimeInMillis / 1000).toLong

      // bypass the balance checks during unit testing
      if (testMode)
        totalBalance += 1000

      if (totalBalance > 0) {
        val toSubtract = 0L - totalBalance

        // Add input as bundle entry
        // Only a single entry, signatures will be added later
        bundle.addEntry(securitySum, inputAddress, toSubtract, tag, timestamp)
      }

      // Return not enough balance error
      if (data._1 > totalBalance) {
        throw new IllegalStateException("Not enough balance")
      }

      // If there is a remainder value
      // Add extra output to send remaining funds to
      if (totalBalance > data._1) {
        val remainder = totalBalance - data._1

        // Remainder bundle entry if necessary
        if (remainderAddress.isEmpty) {
          throw new IllegalStateException("No remainder address defined")
        }

        bundle.addEntry(1, remainderAddress, remainder, tag, timestamp)
      }

      bundle.finalize(customCurl.clone())
      bundle.addTrytes(data._2)

      return bundle.transactions
    } else {
      throw new RuntimeException("Invalid value transfer: the transfer does not require a signature.")
    }
  }

  /**
    * @param hash The hash.
    * @return A bundle.
    * @throws ArgumentException is thrown when an invalid argument is provided.
    */
  @throws[ArgumentException]
  def findTailTransactionHash(hash: String): String = {
    val trx: Transaction = getTrytes(hash) match {
      case None => throw new ArgumentException("Invalid hash")
      case Some(gtr) if gtr.trytes.isEmpty => throw new ArgumentException("Bundle transactions not visible")
      case Some(gtr) =>Transaction(gtr.trytes(0), customCurl.clone())
    }

    if (trx.bundle.isEmpty) throw new ArgumentException("Invalid trytes, could not create object")

    if (trx.currentIndex == 0) trx.hash
    else findTailTransactionHash(trx.bundle)
  }

  /**
    * @param seed               Tryte-encoded seed.
    * @param security           The security level of private key / seed.
    * @param inputs             List of inputs used for funding the transfer.
    * @param bundle             To be populated.
    * @param tag                The tag.
    * @param totalValue         The total value.
    * @param remainderAddress   If defined, this address will be used for sending the remainder value (of the inputs) to.
    * @param signatureFragments The signature fragments.
    * @throws NotEnoughBalanceException     is thrown when a transfer fails because their is not enough balance to perform the transfer.
    * @throws InvalidSecurityLevelException is thrown when the specified security level is not valid.
    * @throws InvalidAddressException       is thrown when the specified address is not an valid address.
    */
  @throws[NotEnoughBalanceException]
  @throws[InvalidSecurityLevelException]
  @throws[InvalidAddressException]
  def addRemainder(seed: String, security: Int, inputs: List[Input], bundle: Bundle, tag: String, totalValue: Long,
                   remainderAddress: String, signatureFragments: List[String]): List[String] = {
    var totalTransferValue = totalValue

    for (i <- inputs.indices) {
      val thisBalance = inputs(i).balance
      val toSubtract = 0 - thisBalance
      val timestamp = Math.floor(Calendar.getInstance().getTimeInMillis / 1000).toLong

      // Add input as bundle entry
      bundle.addEntry(security, inputs(i).address, toSubtract, tag, timestamp)

      // If there is a remainder value
      // Add extra output to send remaining funds to
      if (thisBalance >= totalTransferValue) {
        val remainder = thisBalance - totalTransferValue

        // If user has provided remainder address
        // Use it to send remaining funds to
        if (remainder > 0 && remainderAddress.nonEmpty) {
          // Remainder bundle entry
          bundle.addEntry(1, remainderAddress, remainder, tag, timestamp)

          // Final function for signing inputs
          return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone())
        } else if (remainder > 0) {
          // Generate a new Address by calling getNewAddress
          val res = getNewAddress(seed, security, 0, false, 0, false)

          // Remainder bundle entry
          bundle.addEntry(1, res.addresses(0), remainder, tag, timestamp)

          // Final function for signing inputs
          return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone())
        } else {
          // If there is no remainder, do not add transaction to bundle
          // simply sign and return
          return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone())
        }
      } else {
        // If multiple inputs provided, subtract the totalTransferValue by
        // the inputs balance
        totalTransferValue -= thisBalance
      }
    }

    throw new NotEnoughBalanceException()
  }

  def messageToFragments(message: String, fragments: Seq[String]): Seq[String] = message match {
    case message if message.isEmpty => fragments
    case message => messageToFragments(message.substring(2187, message.length), fragments ++ Seq(message.substring(0, 2187).padTo(2187, "9").mkString("")))
  }

}

object IotaAPI {

  def apply(): IotaAPI = new IotaAPI {
    override val customCurl: SCurl = SCurl()
    override val service: IotaAPIService = IotaAPIService()
  }

}
