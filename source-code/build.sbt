import Dependencies._

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "de.codecentric"
ThisBuild / organizationName := "codecentric"

lazy val root = (project in file("."))
  .settings(
    name := "composition",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Ywarn-adapted-args",
      "-Ywarn-value-discard",
      "-Ywarn-inaccessible",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Ypartial-unification"
    ),
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8"),
    libraryDependencies ++= libs,
    libraryDependencies ++= testLibs,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck,
      "-minSuccessfulTests",
      "200",
      "-workers",
      "4")
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
