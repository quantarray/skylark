import sbt._
import Keys._

object Build
{
  val organization = "com.quantarray"

  val version = "0.4.0"

  val scalaVersion = "2.11.7"

  val scalaParserCombinatorsVersion = "1.0.2"
  val scalaXmlVersion = "1.0.2"

  val akkaVersion = "2.3.6"
  val breezeVersion = "0.11.2"
  val c3p0Version = "0.9.5-pre10"
  val commonsMath3Version = "3.2"
  val dispatchVersion = "0.11.2"
  val jodaConvertVersion = "1.5"
  val jodaTimeVersion = "2.3"
  val logbackClassicVersion = "1.0.13"
  val phantomDslVersion = "1.5.0"
  val scalaCsvVersion = "1.2.1"
  val scalacticVersion = "2.2.1"
  val scalameterVersion = "0.6"
  val scalaMockScalaTestSupportVersion = "3.1.4"
  val scalatestVersion = "2.2.1"
  val scallopVersion = "0.9.5"
  val slf4jApiVersion = "1.7.5"
  val slickVersion = "2.1.0"
  val slickExtensionsVersion = "2.1.0"
  val sprayVersion = "1.3.2"
  val sprayJsonVersion = "1.3.0"

  /**
   * Resolve others' artifacts.
   */
  val websudosReleasesRepo = "Websudos Releases Repository" at "https://dl.bintray.com/websudos/oss-releases/"
}