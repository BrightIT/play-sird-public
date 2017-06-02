name := "play-sird"
organization := "com.brightit"

version := "0.9-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.4"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.9" % "test"
)

publishTo := {
  val nexus = "http://repo.bright-it.net/content/repositories/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "snapshots")
  else
    Some("releases"  at nexus + "releases")
}
