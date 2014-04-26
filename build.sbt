name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

parallelExecution := true

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1.5",
  "org.springframework" % "spring-core" % "4.0.3.RELEASE",
  "org.springframework" % "spring-beans" % "4.0.3.RELEASE",
  "org.springframework" % "spring-context" % "4.0.3.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.3.RELEASE",
  "com.google.javascript" % "closure-compiler" % "v20130411",
  "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7"
)

play.Project.playJavaSettings
