This plugin is used to enable VI mode in Ammonite repl, 
## How to Use
Edit `~/.ammonite/predef.scala`

```
interp.load.ivy("com.github.chengpohi" %% "ammonite-vi" % "1.2")
@
val shellSession = ammonite.shell.ShellSession()
import shellSession._
import ammonite.shell.PPrints._
import ammonite.ops._
import scala.io._
repl.frontEnd() match {
  case ammoniteFrontEnd: ammonite.repl.AmmoniteFrontEnd => { 
    com.github.chengpohi.VIMode(repl, wd)
  }
  case _ => ammonite.shell.Configure(repl, wd)
}
```


## Support shortcuts

|ShortCut | Function|
|:----|:----|
|Esc + Enter |Toggle VI Model|
|Esc|Toggle Visual Mode|
|h|move to left char|
|l|move to right char|
|i|insert mode|
|a|insert mode|
|x|delete current char|
|0|go to the start of line|
|d + d|delete current line|
|$|go to the end of line|
|b|go to the left word|
|w|go to the next word|
|j|go to the next history|
|k|go to the previous history|
