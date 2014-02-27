name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

parallelExecution := true

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "pl.matisoft" %% "play-soy-view" % "0.1-SNAPSHOT",
  "org.springframework" % "spring-core" % "4.0.1.RELEASE",
  "org.springframework" % "spring-beans" % "4.0.1.RELEASE",
  "org.springframework" % "spring-context" % "4.0.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.1.RELEASE",
  "com.google.javascript" % "closure-compiler" % "v20130411",
  "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7",
  cache
)

play.Project.playJavaSettings
