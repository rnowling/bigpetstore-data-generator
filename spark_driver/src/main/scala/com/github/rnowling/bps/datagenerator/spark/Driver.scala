package com.github.rnowling.bps.datagenerator.spark

import com.github.rnowling.bps.datagenerator.datamodels.{Store,Customer,PurchasingProfile,Transaction}
import com.github.rnowling.bps.datagenerator.{DataLoader,StoreGenerator,CustomerGenerator,PurchasingProfileGenerator,TransactionGenerator}
import com.github.rnowling.bps.datagenerator.framework.SeedFactory
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import java.util.ArrayList
import scala.util.Random
import java.io.File
import java.util.Date

object SparkDriver {
  private var nStores: Int = -1
  private var nCustomers: Int = -1
  private var simulationLength: Double = -1.0
  private var seed: Long = -1
  private var outputDir: File = new File(".")

  private val NPARAMS = 5

  private def printUsage() {
    val usage: String = "BigPetStore Data Generator\n" +
      "\n" +
      "Usage: spark-submit ... outputDir nStores nCustomers simulationLength [seed]\n" +
      "\n" + 
      "outputDir - (string) directory to write files\n" +
      "nStores - (int) number of stores to generate\n" +
      "nCustomers - (int) number of customers to generate\n" +
      "simulationLength - (float) number of days to simulate\n" +
      "seed - (long) seed for RNG. If not given, one is reandomly generated.\n"

    println(usage)
  }

  private def parseArgs(args: Array[String]) {
    if(args.length != NPARAMS && args.length != (NPARAMS - 1)) {
      printUsage()
      System.exit(1)
    }

    var i = 0

    outputDir = new File(args(i))
    if(! outputDir.exists()) {
      System.err.println("Given path (" + args(i) + ") does not exist.\n")
      printUsage()
      System.exit(1)
    }

    if(! outputDir.isDirectory()) {
      System.err.println("Given path (" + args(i) + ") is not a directory.\n")
      printUsage()
      System.exit(1)
    }

    i += 1
    try {
      nStores = args(i).toInt
    }
    catch {
      case _ : NumberFormatException =>
        System.err.println("Unable to parse '" + args(i) + "' as an integer for nStores.\n")
        printUsage()
        System.exit(1)
    }

    i += 1
    try {
      nCustomers = args(i).toInt
    }
    catch {
      case _ : NumberFormatException =>
        System.err.println("Unable to parse '" + args(i) + "' as an integer for nCustomers.\n")
        printUsage()
        System.exit(1)
    }

    i += 1
    try {
      simulationLength = args(i).toDouble
    }
    catch {
      case _ : NumberFormatException =>
        System.err.println("Unable to parse '" + args(i) + "' as a float for simulationLength.\n")
        printUsage()
        System.exit(1)
    }

    if(args.length == NPARAMS) {
      i += 1
      try {
        seed = args(i).toLong
      }
      catch {
        case _ : NumberFormatException =>
          System.err.println("Unable to parse '" + args(i) + "' as a long for seed.\n")
          printUsage()
          System.exit(1)
      }
    }
    else {
      seed = (new Random()).nextLong
    }
  }


  def main(args: Array[String]) {
    parseArgs(args)

    val inputData = new DataLoader().loadData()
    val seedFactory = new SeedFactory(seed);

    println("Generating stores...")
    val stores : ArrayList[Store] = new ArrayList()
    val storeGenerator = new StoreGenerator(inputData, seedFactory);
    for(i <- 1 to nStores) {
      val store = storeGenerator.generate()
      stores.add(store)
    }
    println("Done.")

    println("Generating customers...")
    var customers: List[Customer] = List()
    val custGen = new CustomerGenerator(inputData, stores, seedFactory)
    for(i <- 1 to nCustomers) {
      val customer = custGen.generate()
      customers = customer :: customers
    }
    println("Done.")

    println("Creating SparkConf")
    val conf = new SparkConf().setAppName("BPS Data Generator")

    println("Creating SparkContext")
    val sc = new SparkContext(conf)

    println("Broadcasting stores and products")
    val storesBC = sc.broadcast(stores)
    val productBC = sc.broadcast(inputData.getProductCategories())
    val customerRDD = sc.parallelize(customers)
    val nextSeed = seedFactory.getNextSeed()

    println("Defining transaction DAG")
    val transactionRDD = customerRDD.mapPartitionsWithIndex { (index, custIter) =>
      val seedFactory = new SeedFactory(nextSeed ^ index)
      val transactionIter = custIter.map{ customer =>
	val products = productBC.value

        val profileGen = new PurchasingProfileGenerator(products, seedFactory)
        val profile = profileGen.generate()

        val transGen = new TransactionGenerator(customer, profile, storesBC.value, products,
          seedFactory)

        var transactions : List[Transaction] = List()
	var transaction = transGen.generate()
        while(transaction.getDateTime() < simulationLength) {
          transactions = transaction :: transactions

          transaction = transGen.generate()
        }

	transactions
      }
      transactionIter
    }.flatMap( s => s)

    println("Generating transactions...")
    val nTrans = transactionRDD.count()
    println(s"Generated $nTrans transactions.")

    val initialDate : Long = new Date().getTime()

    val transactionStringsRDD = transactionRDD.map { t =>
      var records : List[String] = List()
      val products = t.getProducts()
      for(i <- 0 until products.size()) {
        val p = products.get(i)
	val name = t.getCustomer().getName()
        val custLocation = t.getCustomer().getLocation()
        val storeLocation = t.getStore().getLocation()

        // days -> milliseconds = days * 24 h / day * 60 min / hr * 60 sec / min * 1000 ms / sec
        val dateMS = (t.getDateTime * 24.0 * 60.0 * 60.0 * 1000.0).toLong
        val date = new Date(initialDate + dateMS)


        var record = ""
        record += t.getStore().getId() + ","
        record += storeLocation.getZipcode() + ","
        record += storeLocation.getCity() + ","
        record += storeLocation.getState() + ","

        record += t.getCustomer().getId() + ","
	record += name.getFirst() + "," + name.getSecond() + ","
	record += custLocation.getZipcode() + ","
	record += custLocation.getCity() + ","
	record += custLocation.getState() + ","

        record += t.getId() + ","
        record += date + ","
	record += p

        records = record :: records
      }

      records
    }.flatMap { s => s }

    transactionStringsRDD.saveAsTextFile(outputDir + "/transactions.txt")

    sc.stop()
  }
}
