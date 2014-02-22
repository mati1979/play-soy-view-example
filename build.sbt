name := "play-soy-view-example"

version := "1.0-SNAPSHOT"

publishMavenStyle := true

parallelExecution := true

libraryDependencies ++= Seq(
  "com.github.mati1979" %% "play-soy-view" % "0.1",
  "org.springframework" % "spring-core" % "4.0.1.RELEASE",
  "org.springframework" % "spring-beans" % "4.0.1.RELEASE",
  "org.springframework" % "spring-context" % "4.0.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.0.1.RELEASE",
  "com.google.javascript" % "closure-compiler" % "v20130411",
  "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7",
  cache
)

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>http://your.project.url</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:your-account/your-project.git</url>
      <connection>scm:git:git@github.com:your-account/your-project.git</connection>
    </scm>
    <developers>
      <developer>
        <id>you</id>
        <name>Your Name</name>
        <url>http://your.url</url>
      </developer>
    </developers>
  )

play.Project.playJavaSettings
