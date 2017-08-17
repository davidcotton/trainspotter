name := """trainspotter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "mysql" % "mysql-connector-java" % "5.1.40",
  "org.projectlombok" % "lombok" % "1.16.10",
  "io.atlassian.fugue" % "fugue" % "4.5.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P25-B4-SNAPSHOT",
  "be.objectify" %% "deadbolt-java" % "2.5.4",
  "com.clever-age" % "play2-elasticsearch" % "2.1-SNAPSHOT",
  "org.mockito" % "mockito-core" % "2.1.0",
  "io.rest-assured" % "scala-support" % "3.0.2"
)
