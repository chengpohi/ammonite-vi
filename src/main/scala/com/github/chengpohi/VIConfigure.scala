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
    val viHistoryFilter = VIHistoryFilter(() => repl.fullHistory.reverse)

    repl.frontEnd() = ammonite.repl.frontend.AmmoniteFrontEnd(
      extraFilters orElse
      viHistoryFilter orElse
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
