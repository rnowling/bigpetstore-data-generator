name := "bps-datagenerator-sparkdriver"

version := "0.2"

scalaVersion := "2.10.4"

resolvers += Resolver.bintrayRepo("rnowling", "bigpetstore")

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0"

libraryDependencies += "com.github.rnowling.bigpetstore" % "bigpetstore-data-generator" % "0.2"


