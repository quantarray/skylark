name := "skylark-autodiff"

organization := Build.organization

version := Build.version

scalaVersion := Build.scalaVersion

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature" /*, "-Ymacro-debug-lite"*/)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % Build.scalaVersion,
  "org.scala-lang" % "scala-reflect" % Build.scalaVersion,
  "joda-time" % "joda-time" % Build.jodaTimeVersion,
  "org.joda" % "joda-convert" % Build.jodaConvertVersion,
  "org.scalatest" % "scalatest_2.11" % Build.scalatestVersion % "test"
)

useGpg := true

usePgpKeyHex("389FB928")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version
{ v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository :=
  { x => false }

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
