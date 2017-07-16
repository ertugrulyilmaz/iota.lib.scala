name := "sota"
organization := "org.iota"
version := "=0.9.1-SNAPSHOT"
description := "SOTA library is a simple Scala wrapper around IOTA Node's JSON-REST HTTP interface."

scalaVersion := "2.12.2"

scalacOptions := Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

libraryDependencies ++= {
  val akkaV = "2.5.2"
  val akkaHttpV = "10.0.9"
  val akkaHttpSprayJsonV = "10.0.9"
  val commonsLang3V = "3.3.2"
  val logbackV = "1.1.9"
  val mockitoV = "1.10.19"
  val nettyV = "3.10.6.Final"
  val ningV = "1.9.40"
  val scalaLoggingV = "3.5.0"
  val scalaTestV = "3.0.1"
  val sprayJsonV = "1.3.3"

  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpSprayJsonV,
    "org.apache.commons" % "commons-lang3" % commonsLang3V,
    "com.ning" % "async-http-client" % ningV,
    "io.netty" % "netty" % nettyV,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV,
    "ch.qos.logback" % "logback-core" % logbackV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "io.spray" %% "spray-json" % sprayJsonV,
    "org.mockito" % "mockito-all" % mockitoV % Test,
    "org.scalatest" %% "scalatest" % scalaTestV % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % Test
  )
}

jarName in assembly := s"${name.value}.jar"
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_ *) => MergeStrategy.discard
  case _ => MergeStrategy.first
}