organization := "me.lessis"

name := "docker-watch"

version := "0.1.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.4", "2.11.2")

scalaVersion := crossScalaVersions.value.last

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

libraryDependencies += "me.lessis" %% "tugboat" % "0.2.0"
