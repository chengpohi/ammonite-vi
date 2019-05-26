This plugin is used to enable vi key bindings for Ammonite repl, 

## SetUp
Edit `~/.ammonite/predef.sc`

```
//1.6.1 version support ammonite repl 1.6.1 version
interp.load.ivy("com.github.chengpohi" %% "ammonite-vi" % "1.6.1")

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
