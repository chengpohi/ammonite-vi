organization := "com.github.chengpohi"

name := "ammonite-vi"

version := "2.X-SNAPSHOT"

publishMavenStyle := true

scalaVersion := "2.13.4"

libraryDependencies ++= {
  Seq(
    "com.lihaoyi" % "ammonite-repl_2.13.4" % "2.3.8-32-64308dc3" % "provided",
    "com.lihaoyi" % "ammonite-shell_2.13.4" % "2.3.8-32-64308dc3" % "provided"
  )
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishConfiguration := publishConfiguration.value.withOverwrite(true)

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

credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "FB278B5E847B2AF4BEDD7F4EABD0514E703CEFDB", // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)
