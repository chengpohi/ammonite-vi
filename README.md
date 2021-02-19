This plugin is used to enable vi key bindings for Ammonite repl, 

## Compatible Version
Scala: 2.13
Ammonite: 2.X

## SetUp
Edit `~/.ammonite/predef.sc`

```
interp.load.ivy("com.github.chengpohi" %% "ammonite-vi" % "2.0")

@


repl.frontEnd() match {
  case ammoniteFrontEnd: ammonite.repl.AmmoniteFrontEnd => {
    com.github.chengpohi.VIMode(repl)
    println("VI Mode Enabled")
  }
  case _ =>
}
```


ESC + ENTER: switch vi mode

