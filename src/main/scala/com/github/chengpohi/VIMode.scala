package com.github.chengpohi

import ammonite.repl.api.ReplAPI
import ammonite.terminal.Filter
import com.github.chengpohi.ViFilters.viFilters

/**
 * ammonite vi
 * Created by chengpohi on 12/6/15.
 */
object VIMode {
  def apply(repl: ReplAPI,
            extraFilters: Filter = Filter.empty): Unit = {


    repl.frontEnd() =
      ammonite.repl.AmmoniteFrontEnd(
        ammonite.compiler.Parsers,
        Filter.merge(
          extraFilters,
          viFilters(repl)
        ))
  }

}
