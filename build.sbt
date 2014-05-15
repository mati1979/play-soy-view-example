name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.8",
  "org.springframework" % "spring-core" % "4.0.3.RELEASE",
  "org.springframework" % "spring-beans" % "4.0.3.RELEASE",
  "org.springframework" % "spring-context" % "4.0.3.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.3.RELEASE"
)

play.Project.playJavaSettings
