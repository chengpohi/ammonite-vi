This plugin is used to enable Vi mode in Ammonite 1.6 repl, 
## SetUp
Edit `~/.ammonite/predef.sc`

```
interp.load.ivy("com.github.chengpohi" %% "ammonite-vi" % "1.6-1-SNAPSHOT")

@

val shellSession = ammonite.shell.ShellSession()
import shellSession._
repl.frontEnd() match {
  case ammoniteFrontEnd: ammonite.repl.AmmoniteFrontEnd => {
    println("VI Mode Enabled")
    com.github.chengpohi.VIMode(repl, wd)
  }
  case _ =>
}
```
