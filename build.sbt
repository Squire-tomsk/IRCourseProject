name := "IRSystem"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.gaocegege" % "scrala" % "0.1.5"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % "3.2"
)