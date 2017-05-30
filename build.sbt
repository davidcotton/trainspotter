name := """trainspotter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, SbtWeb)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "mysql" % "mysql-connector-java" % "5.1.40",
  "org.projectlombok" % "lombok" % "1.16.10",
  "io.atlassian.fugue" % "fugue" % "4.5.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.kalium" % "kalium" % "0.6.0",
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "react" % "15.3.2",
  "org.webjars" % "react-bootstrap" % "0.28.1",
  "org.mockito" % "mockito-core" % "2.1.0",
  "io.rest-assured" % "scala-support" % "3.0.2"
)
