name := "IRSystem"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.6.1",
  "com.github.gaocegege" % "scrala" % "0.1.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "net.debasishg" %% "redisclient" % "3.2",
  "org.scalikejdbc" %% "scalikejdbc" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config"  % "2.5.0",
  "org.postgresql" % "postgresql" % "9.4.1211",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)