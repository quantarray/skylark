name := "skylark"

organization := Build.organization

scalaVersion := Build.scalaVersion

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

lazy val `skylark-time` = project

lazy val `skylark-natural-language` = project

lazy val `skylark-db` = project

lazy val `skylark-timeseries` = project.dependsOn(`skylark-time`)

lazy val `skylark-timeseries-cassandra` = project.dependsOn(`skylark-time`, `skylark-timeseries`, `skylark-db`)

lazy val `skylark-measure` = project.dependsOn(`skylark-natural-language`)

lazy val `skylark-measure-market` = project.dependsOn(`skylark-time`, `skylark-measure`)

lazy val `skylark-learning` = project

lazy val `skylark-learning-neural` = project.dependsOn(`skylark-learning`)

lazy val skylark = project.in(file(".")).aggregate(`skylark-time`, `skylark-natural-language`, `skylark-db`, `skylark-timeseries`, `skylark-timeseries-cassandra`, `skylark-measure`, `skylark-measure-market`, `skylark-learning`, `skylark-learning-neural`)

fork in run := true
