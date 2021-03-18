name := "akka-sharding"

version := "0.1"

scalaVersion := "2.12.13"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

val AkkaVersion = "2.6.12"


lazy val akkaVersion = "2.6.12"
lazy val akkaHttpVersion = "10.2.3"
lazy val akkaGrpcVersion = "1.1.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.3" % Test,

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,

  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-pki" % akkaVersion,

  "io.grpc" % "grpc-netty" % "1.36.0",

  //"com.thesamet.scalapb" %% "compilerplugin" % "0.10.10",

  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion
)

/*Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)*/

enablePlugins(AkkaGrpcPlugin)
