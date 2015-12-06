package com.github.chengpohi

import ammonite.repl.frontend.ReplAPI
import com.github.chengpohi.VIFilters.{viFilter, VIHistoryFilter}

/**
 * ammonite
 * Created by chengpohi on 12/6/15.
 */
object VIConfigure {
  def apply(repl: ReplAPI, wd: => ammonite.ops.Path) = {
    repl.frontEnd() = ammonite.repl.frontend.AmmoniteFrontEnd(
      viFilter orElse
      VIHistoryFilter(repl.fullHistory)
    )

    repl.prompt.bind(
      sys.props("user.name") +
        "-" +
        wd.segments.lastOption.getOrElse("") +
        "@ "
    )
  }

}
