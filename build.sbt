import com.typesafe.sbt.web.SbtWeb
import play.PlayJava

name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

scalaVersion := "2.10.4"

resolvers += Resolver.file("Local repo", file(System.getProperty("user.home") + "/.ivy2/local"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.19-SNAPSHOT"
  //"com.google.inject" % "guice" % "4.0-beta5"
)
