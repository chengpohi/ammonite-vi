package com.github.chengpohi

import ammonite.repl.{AmmoniteFrontEnd, ReplAPI}
import ammonite.terminal.Filter
import com.github.chengpohi.ViFilters.viFilters

/**
  * ammonite
  * Created by chengpohi on 12/6/15.
  */
object ViMode {
  def apply(repl: ReplAPI, wd: => ammonite.ops.Path, extraFilters: Filter = Filter.empty) = {
    repl.frontEnd() = AmmoniteFrontEnd(
      Filter.merge(
        extraFilters,
        viFilters(repl)
      )
    )
  }

}
