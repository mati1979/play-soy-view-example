name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.12",
  "org.springframework" % "spring-core" % "4.0.3.RELEASE",
  "org.springframework" % "spring-beans" % "4.0.3.RELEASE",
  "org.springframework" % "spring-context" % "4.0.3.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.3.RELEASE",
  "com.threerings" % "react" % "1.4.1",
  javaWs
)
