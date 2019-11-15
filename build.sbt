
name := """nginx-dsl"""
organization := "de.bstumm"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
)
