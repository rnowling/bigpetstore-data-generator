package com.github.rnowling.bps.datagenerator.spark

import com.github.rnowling.bps.datagenerator.cli._
import com.github.rnowling.bps.datagenerator.datamodels.outputs.{Customer,Transaction}
import com.github.rnowling.bps.datagenerator.TransactionGenerator
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile
import com.github.rnowling.bps.datagenerator.framework.SeedFactory
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.util.Random

object SparkDriver {
  def main(args: Array[String]) {
     val driver: Driver = new Driver()
     driver.parseArgs(args)
     
     val inputData = driver.loadData()

     val simulation = driver.buildSimulation(inputData)
     simulation.generateStores()
     simulation.generateCustomers()
     simulation.generatePurchasingProfiles()

     val conf = new SparkConf()
       .setAppName("BPS Data Generator")
	 
     val sc = new SparkContext(conf)

     val stores = sc.broadcast(simulation.getStores())
     val products = sc.broadcast(simulation.getInputData().getProductCategories())

     val scalaCustomers: List[Customer] = List() ++ simulation.getCustomers().asScala
     val scalaProfiles: List[PurchasingProfile] = List() ++ simulation.getPurchasingProfiles().asScala

     val customers = sc.parallelize(scalaCustomers).zipWithIndex().map { case (c, l) => (l, c) }
     val profiles = sc.parallelize(scalaProfiles).zipWithIndex().map { case (c, l) => (l, c) }

     val customerProfiles = customers.join(profiles)

     val seed = new Random().nextLong()

     val simulationLength = driver.getSimulationLength()

     val transactionRDD = customerProfiles.mapPartitionsWithIndex { (index, iter) =>
         val seedFactory = new SeedFactory(seed ^ index)
         val transactionIter = iter.map{ case (_, (customer, profile)) => 
           val transGen = new TransactionGenerator(customer, profile, stores.value, products.value, 
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

      val nTrans = transactionRDD.count()

      println(s"Generated $nTrans transactions.")

      sc.stop()
  }

}