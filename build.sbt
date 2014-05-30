name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.4"

val springVersion = settingKey[String]("4.0.5.RELEASE")

springVersion := "4.0.5.RELEASE"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.13.play23",
  "org.springframework" % "spring-core" % springVersion.value,
  "org.springframework" % "spring-beans" % springVersion.value,
  "org.springframework" % "spring-context" % springVersion.value,
  "org.springframework" % "spring-expression" % springVersion.value
)
