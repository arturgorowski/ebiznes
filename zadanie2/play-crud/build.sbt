name := "play-crud"
organization := "pl.edu.uj"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.jcenterRepo

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.13"

libraryDependencies ++= Seq(ehcache, ws, specs2 % Test, guice)
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial" % "sqlite-jdbc" % "3.30.1",
  "com.iheart" %% "ficus" % "1.4.7",
  "com.mohiva" %% "play-silhouette" % "6.1.1",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "6.1.1",
  "com.mohiva" %% "play-silhouette-persistence" % "6.1.1",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "6.1.1",
  "com.mohiva" %% "play-silhouette-totp" % "6.1.1",
  "net.codingwell" %% "scala-guice" % "4.2.6"
)

PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.jdk7(play.sbt.run.toLoggerProxy(sLog.value))
resolvers += "Atlassian's Maven Public Repository" at "https://packages.atlassian.com/maven-public/"

