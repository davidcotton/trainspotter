name := """trainspotter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

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
  "org.mockito" % "mockito-core" % "2.1.0",
  "io.rest-assured" % "scala-support" % "3.0.2"
)
