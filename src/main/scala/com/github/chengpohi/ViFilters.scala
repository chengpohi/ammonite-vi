package com.github.chengpohi

import ammonite.repl.ReplAPI
import ammonite.terminal.FilterTools._
import ammonite.terminal._
import ammonite.terminal.filters.ReadlineFilters.CutPasteFilter
import ammonite.terminal.filters.{GUILikeFilters, HistoryFilter}

/**
  * Ammonite VI Mode
  * Created by chengpohi on 12/5/15.
  */
object ViFilters {
  var VI_MODE = true
  var VISUAL_MODE = true
  lazy val cutPasteFilter = CutPasteFilter()

  def viFilters(repl: ReplAPI) = Filter.merge(

    viKeysFilter,
    viSingleKeyFilter,
    viNavFilter,
    viEditModeFilter,
    ViHistoryFilter(() => repl.fullHistory.reverse, repl.codeColorsImplicit.comment),
  )

  def viKeysFilter: Filter = Filter.merge(
    Filter.action(Seq("27", "13")) {
      case TermState(rest, b, c, _) => {
        VI_MODE = !VI_MODE
        VISUAL_MODE = VI_MODE
        TS(rest, b, c)
      }
    },
    Filter.action(Seq("27", "10")) {
      case TermState(rest, b, c, _) => {
        VI_MODE = !VI_MODE
        VISUAL_MODE = VI_MODE
        TS(rest, b, c)
      }
    }
  )

  def viSingleKeyFilter: Filter = Filter.action(Seq("27")) {
    case TermState(rest, b, c, _) if VI_MODE =>
      VISUAL_MODE = true
      TS(rest, b, c)
  }

  def viEditModeFilter: Filter = Filter.merge(
    Filter.action("i") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        TS(rest, b, c)
    }
    ,

    Filter.action("a") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        TS(rest, b, c + 1)
    }
    ,
    Filter.action("x") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b patch(from = c, patch = Nil, replaced = 1), c)
    }
    ,

    Filter.action(Seq("d", "d")) {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b.take(0), 0)
    }
    ,
    Filter.action(Seq("d", "w")) {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right: (Vector[Char], Int) = cutPasteFilter.cutWordRight(b, c)
        TS(rest, right._1, right._2)
    }
    ,
    Filter.action(Seq("c", "w")) {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        val right: (Vector[Char], Int) = cutPasteFilter.cutWordRight(b, c)
        TS(rest, right._1, right._2)
    }
    ,
    Filter.action("D") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right: (Vector[Char], Int) = cutPasteFilter.cutLineRight(b, c)
        TS(rest, right._1, right._2)
    }
  )

  def viNavFilter: Filter = Filter.merge(
    Filter.action("h") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, c - 1)
    },
    Filter.action("l") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, c + 1)
    },

    Filter.action("0") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, 0)
    },

    Filter.action("b") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val left = GUILikeFilters.wordLeft(b, c)
        TS(rest, left._1, left._2)
    },

    Filter.action("w") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right = GUILikeFilters.wordRight(b, c)
        TS(rest, right._1, right._2)
    },
    Filter.action(":") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, b.size)
    }
  )


  case class ViHistoryFilter(history: () => IndexedSeq[String], comment: fansi.Attrs) extends DelegateFilter {
    val historyFilter = new HistoryFilter(history, comment)
    var oldHistoryLength: Int = history().length
    override def filter: Filter = Filter.merge(
      Filter.action("k", ti => historyFilter.searchOrHistoryAnd(firstRowInfo(ti))) {
        case ts if VISUAL_MODE =>
          historyFilter.wrap(ts.inputs, historyFilter.up(ts.buffer, ts.cursor))
      },
      Filter.action("j", ti => historyFilter.searchOrHistoryAnd(firstRowInfo(ti))) {
        case ts if VISUAL_MODE =>
          historyFilter.wrap(ts.inputs, historyFilter.up(ts.buffer, ts.cursor))
          historyFilter.wrap(ts.inputs, historyFilter.down(ts.buffer, ts.cursor))
      }
    )
  }

}
