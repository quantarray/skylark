import sbt.Keys._
import sbt._

val projectVersion = "0.13.2"

val compilerVersion = "2.11.8"

val scalaParserCombinatorsVersion = "1.0.2"
val scalaReflectVersion = compilerVersion
val scalaMacrosParadiseVersion = "2.1.0"
val scalaXmlVersion = "1.0.2"

val logbackClassicVersion = "1.0.13"
val scalacticVersion = "2.2.1"
val scalameterVersion = "0.6"
val scalaMockScalaTestSupportVersion = "3.2"
val scalatestVersion = "2.2.1"
val slf4jApiVersion = "1.7.5"

lazy val commonSettings = Seq(
  organization := "com.quantarray",

  version := projectVersion,

  scalaVersion := compilerVersion,

  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature"),

  // Due to this flag value "true" test cases fail in sbt, need to find alternative way.
  packageOptions in(Compile, packageBin) += Package.ManifestAttributes(java.util.jar.Attributes.Name.SEALED -> "false"),

  unmanagedBase := baseDirectory.value / ".." / "lib",

  updateOptions := updateOptions.value.withCachedResolution(true),

  logBuffered := false,

  parallelExecution in Test := false,

  resolvers += Resolver.bintrayRepo("jetbrains", "teamcity-rest-client")
)

lazy val `skylark-measure` = (project in file("skylark-measure")).
  settings(commonSettings: _*).
  settings(
    name := "skylark-measure",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" % "scala-parser-combinators_2.11" % scalaParserCombinatorsVersion,
      "org.slf4j" % "slf4j-api" % slf4jApiVersion,
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "org.scalatest" % "scalatest_2.11" % scalatestVersion % "test"
    ),

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

lazy val skylark = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "skylark",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-skylark")).
  aggregate(
    `skylark-measure`
  )
