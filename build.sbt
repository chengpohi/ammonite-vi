organization := "com.github.chengpohi"

name := "ammonite-vi"

version := "1.6-1-SNAPSHOT"

publishMavenStyle := true

scalaVersion := "2.12.8"

libraryDependencies ++= {
  Seq(
    "com.lihaoyi" % "ammonite-repl_2.12.8" % "1.6.7-1-a44339b",
    "com.lihaoyi" % "ammonite-shell_2.12.8" % "1.6.7-1-a44339b"
  )
}


publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}


pomExtra := (
  <url>https://github.com/chengpohi/ammonite-vi</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:chengpohi/ammonite-vi.git</url>
    <connection>scm:git:git@github.com:chengpohi/ammonite-vi.git</connection>
  </scm>
  <developers>
    <developer>
      <id>chengpohi</id>
      <name>chengpohi</name>
      <url>https://github.com/chengpohi/ammonite-vi</url>
    </developer>
  </developers>
)
