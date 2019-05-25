package com.github.chengpohi

import ammonite.repl.{AmmoniteFrontEnd, ReplAPI}
import ammonite.terminal.Filter
import com.github.chengpohi.ViFilters.viFilters

/**
  * ammonite vi
  * Created by chengpohi on 12/6/15.
  */
object VIMode {
  def apply(repl: ReplAPI, wd: => ammonite.ops.Path, extraFilters: Filter = Filter.empty): Unit = {
    repl.frontEnd() = AmmoniteFrontEnd(
      Filter.merge(
        extraFilters,
        viFilters(repl)
      )
    )
  }

}
