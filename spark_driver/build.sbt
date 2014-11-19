import AssemblyKeys._

name := "bigpetstore-spark"

version := "0.2.1"

scalaVersion := "2.10.4"

resolvers += Resolver.bintrayRepo("rnowling", "bigpetstore")

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0" % "provided"

libraryDependencies += "com.github.rnowling.bigpetstore" % "bigpetstore-data-generator" % "0.2.1"

assemblySettings

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}




