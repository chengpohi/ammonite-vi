package com.github.chengpohi

import ammonite.repl.frontend.ReplAPI
import ammonite.terminal.TermCore
import com.github.chengpohi.VIFilters.{VIHistoryFilter, viFilter}

/**
 * ammonite
 * Created by chengpohi on 12/6/15.
 */
object VIConfigure {
  def apply(repl: ReplAPI, wd: => ammonite.ops.Path, extraFilters: TermCore.Filter = PartialFunction.empty) = {
    repl.frontEnd() = ammonite.repl.frontend.AmmoniteFrontEnd(
      extraFilters orElse
      VIHistoryFilter(repl.fullHistory) orElse
      viFilter
    )

    repl.prompt.bind(
      sys.props("user.name") +
        "-" +
        wd.segments.lastOption.getOrElse("") +
        "@ "
    )
  }

}
