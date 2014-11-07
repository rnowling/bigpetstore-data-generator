package com.github.rnowling.bps.datagenerator.spark

import com.github.rnowling.bps.datagenerator.cli.Driver
import com.github.rnowling.bps.datagenerator.datamodels.{Store,Customer,PurchasingProfile,Transaction}
import com.github.rnowling.bps.datagenerator.{StoreGenerator,CustomerGenerator,PurchasingProfileGenerator,TransactionGenerator}
import com.github.rnowling.bps.datagenerator.framework.SeedFactory
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import java.util.ArrayList

object SparkDriver {
  def main(args: Array[String]) {
     val driver: Driver = new Driver()
     driver.parseArgs(args)
     
     val inputData = driver.loadData()
     val nStores = driver.getNStores()
     val nCustomers = driver.getNCustomers()
     val seed = driver.getSeed()
     val simulationLength = driver.getSimulationLength()
  
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

     val conf = new SparkConf()
       .setAppName("BPS Data Generator")
	 
     val sc = new SparkContext(conf)

     val storesBC = sc.broadcast(stores)
     val productBC = sc.broadcast(inputData.getProductCategories())
     val customerRDD = sc.parallelize(customers)
     val nextSeed = seedFactory.getNextSeed()

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

      sc.stop()
  }

}