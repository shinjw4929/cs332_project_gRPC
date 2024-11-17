ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "sortNpartition"
  )

libraryDependencies +=
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"