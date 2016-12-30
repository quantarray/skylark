import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport._

val compilerVersion = "2.11.8"

val scalaParserCombinatorsVersion = "1.0.4"
val scalaMacrosParadiseVersion = "2.1.0"

val catsVersion = "0.8.1"
val commonsMathVersion = "3.6.1"
val logbackClassicVersion = "1.0.13"
val scalacticVersion = "3.0.0"
val scalatestVersion = "3.0.0"
val slf4jApiVersion = "1.7.5"
val sparkVersion = "2.1.0"

lazy val commonSettings = Seq(
  organization := "com.quantarray",

  version := (version in ThisBuild).value,

  scalaVersion := compilerVersion,

  crossScalaVersions := Seq(compilerVersion, "2.12.1"),

  crossVersion := CrossVersion.binary,

  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature"),

  // Due to this flag value "true" test cases fail in sbt, need to find alternative way.
  packageOptions in(Compile, packageBin) += Package.ManifestAttributes(java.util.jar.Attributes.Name.SEALED -> "false"),

  unmanagedBase := baseDirectory.value / ".." / "lib",

  updateOptions := updateOptions.value.withCachedResolution(true),

  logBuffered := false,

  parallelExecution in Test := false,

  resolvers += Resolver.bintrayRepo("jetbrains", "teamcity-rest-client"),

  useGpg := true,

  usePgpKeyHex("389FB928"),

  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),

  publishTo <<= version
  { v: String =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },

  publishMavenStyle := true,

  publishArtifact in Test := false,

  pomIncludeRepository :=
    { x => false },

  pomExtra := <url>http://skylark.io/</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>https://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:quantarray/skylark.git</url>
      <connection>scm:git:git@github.com:quantarray/skylark.git</connection>
    </scm>
    <developers>
      <developer>
        <id>araik</id>
        <name>Araik Grigoryan</name>
        <url>http://www.quantarray.com</url>
      </developer>
    </developers>
)

lazy val `skylark-measure-macros` = (project in file("skylark-measure-macros")).
  settings(commonSettings: _*).
  settings(
    name := "skylark-measure-macros",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.scalamacros" % s"paradise_${scalaVersion.value}" % scalaMacrosParadiseVersion,
      "org.slf4j" % "slf4j-api" % slf4jApiVersion,
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "org.typelevel" %% "cats" % catsVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    ),

    addCompilerPlugin("org.scalamacros" % "paradise" % scalaMacrosParadiseVersion cross CrossVersion.full)
  )

lazy val `skylark-measure` = (project in file("skylark-measure")).
  settings(commonSettings: _*).
  settings(
    name := "skylark-measure",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.scalamacros" % s"paradise_${scalaVersion.value}" % scalaMacrosParadiseVersion,
      "org.slf4j" % "slf4j-api" % slf4jApiVersion,
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "org.apache.commons" % "commons-math3" % commonsMathVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    ),

    addCompilerPlugin("org.scalamacros" % "paradise" % scalaMacrosParadiseVersion cross CrossVersion.full)

  ).dependsOn(`skylark-measure-macros`)

lazy val `skylark-measure-spark` = (project in file("skylark-measure-spark")).
  settings(commonSettings: _*).
  settings(
    name := "skylark-measure-spark",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.slf4j" % "slf4j-api" % slf4jApiVersion,
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    )

  ).dependsOn(`skylark-measure`)

lazy val skylark = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "skylark",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-skylark")).
  aggregate(
    `skylark-measure-macros`, `skylark-measure`, `skylark-measure-spark`
  ).enablePlugins(OrnatePlugin)

releaseTagComment := s"Release ${(version in ThisBuild).value}."

releaseCommitMessage := s"Move to version to ${(version in ThisBuild).value}."

