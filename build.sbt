name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.19-SNAPSHOT"
)
