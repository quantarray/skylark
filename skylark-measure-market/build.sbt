name := "skylark-measure-market"

organization := Build.organization

version := Build.version

scalaVersion := Build.scalaVersion

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature" /*, "-Ymacro-debug-lite"*/)

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-parser-combinators_2.11" % Build.scalaParserCombinatorsVersion,
  "joda-time" % "joda-time" % Build.jodaTimeVersion,
  "org.joda" % "joda-convert" % Build.jodaConvertVersion,
  "org.scalatest" % "scalatest_2.11" % Build.scalatestVersion % "test"
)

publishTo := Build.publishRealm

credentials += Build.publishRealmCredentials