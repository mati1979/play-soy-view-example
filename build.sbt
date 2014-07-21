name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

scalaVersion := "2.10.4"

val springVersion = settingKey[String]("spring version")

springVersion := "4.0.6.RELEASE"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.19-SNAPSHOT",
  "org.springframework" % "spring-core" % springVersion.value,
  "org.springframework" % "spring-beans" % springVersion.value,
  "org.springframework" % "spring-context" % springVersion.value,
  "org.springframework" % "spring-expression" % springVersion.value
)

pipelineStages := Seq(digest, gzip)
