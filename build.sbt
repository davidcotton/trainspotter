name := """trainspotter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, SbtWeb)

scalaVersion := "2.11.7"

resolvers += "keyczar" at "https://raw.githubusercontent.com/google/keyczar/master/java/maven/"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "mysql" % "mysql-connector-java" % "5.1.40",
  "org.projectlombok" % "lombok" % "1.16.10",
  "io.atlassian.fugue" % "fugue" % "4.5.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.abstractj.kalium" % "kalium" % "0.6.0",
  "org.keyczar" % "keyczar" % "0.71h",
  "org.mockito" % "mockito-core" % "2.1.0",
  "io.rest-assured" % "scala-support" % "3.0.2"
)
