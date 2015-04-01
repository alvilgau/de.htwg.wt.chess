import play.Project._

name := """de.htwg.wt.chess"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.2",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.pac4j" % "play-pac4j_java" % "1.2.2",
  "org.pac4j" % "pac4j-oauth" % "1.5.1",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "aopalliance" % "aopalliance" % "1.0"
)

playJavaSettings
