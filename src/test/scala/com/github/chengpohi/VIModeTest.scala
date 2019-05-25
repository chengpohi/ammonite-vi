package com.github.chengpohi

import java.nio.file.Files

import ammonite.Main
import ammonite.shell.ShellSession

object VIModeTest {
  val testPredef =
    """
      |interp.load.ivy("com.github.chengpohi" %% "ammonite-vi" % "1.6-1-SNAPSHOT")
      |
      |@
      |
      |val shellSession = ammonite.shell.ShellSession()
      |import shellSession._
      |repl.frontEnd() match {
      |  case ammoniteFrontEnd: ammonite.repl.AmmoniteFrontEnd => {
      |    println("VI Mode Enabled")
      |    com.github.chengpohi.VIMode(repl, wd)
      |  }
      |  case _ =>
      |}
    """.stripMargin

  def main(args: Array[String]): Unit = {
    val path = Files.createTempFile("predef-sc", ".sc")
    Files.write(path, testPredef.getBytes)
    val session = ShellSession()
    Main.main(Array("--no-home-predef", "-p", path.toString))
  }
}
