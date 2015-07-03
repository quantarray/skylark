name := "skylark"

organization := Build.organization

scalaVersion := Build.scalaVersion

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

lazy val `skylark-time` = project

lazy val `skylark-natural-language` = project

lazy val `skylark-measure` = project.dependsOn(`skylark-natural-language`)

lazy val `skylark-measure-market` = project.dependsOn(`skylark-time`, `skylark-measure`)

lazy val `skylark-learning-neural` = project

lazy val skylark = project.in(file(".")).aggregate(`skylark-natural-language`, `skylark-measure`, `skylark-measure-market`, `skylark-learning-neural`)

fork in run := true
