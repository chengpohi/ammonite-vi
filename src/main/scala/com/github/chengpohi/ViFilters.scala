package com.github.chengpohi

import ammonite.repl.api.ReplAPI
import ammonite.terminal.Filter.{action, partial}
import ammonite.terminal.FilterTools._
import ammonite.terminal.LazyList.~:
import ammonite.terminal._
import ammonite.terminal.filters.BasicFilters.{doEnter, enterFilter, injectNewLine}
import ammonite.terminal.filters.ReadlineFilters.CutPasteFilter
import ammonite.terminal.filters.{BasicFilters, GUILikeFilters, HistoryFilter}

/**
 * Ammonite VI Mode
 * Created by chengpohi on 12/5/15.
 */
object ViFilters {
  var VI_MODE = true
  var VISUAL_MODE = false
  private val ESC_KEY = 27.toChar.toString
  private val ENTER_KEY = 13.toChar.toString
  private val SHIFT_KEY = 16.toChar.toString

  lazy val cutPasteFilter = CutPasteFilter()

  def viFilters(repl: ReplAPI): Filter = Filter.merge(
    viKeysFilter,
    viNavFilter,
    viEditModeFilter,
    ViHistoryFilter(() => repl.fullHistory.reverse, repl.codeColorsImplicit.comment),
  )

  def viKeysFilter: Filter = Filter.merge(
    Filter.action(ESC_KEY + ENTER_KEY) {
      case TermState(rest, b, c, _) => {
        VI_MODE = !VI_MODE
        VISUAL_MODE = VI_MODE
        TS(rest, b, c)
      }
    },
    Filter.action(ESC_KEY) {
      case TermState(rest, b, c, _) if VI_MODE =>
        VISUAL_MODE = true
        TS(rest, b, c)
      case TermState(rest, b, c, _) =>
        TS(rest, b, c)
    }
  )

  def viEditModeFilter: Filter = Filter.merge(
    Filter.action("i") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        TS(rest, b, c)
      case TermState(rest, b, c, _) =>
        VISUAL_MODE = false
        TS(rest, (b.take(c) :+ 'i') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action("a") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        TS(rest, b, c + 1)
      case TermState(rest, b, c, _) =>
        VISUAL_MODE = false
        TS(rest, (b.take(c) :+ 'a') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action("A") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        TS(rest, b, b.size + 1)
      case TermState(rest, b, c, _) =>
        VISUAL_MODE = false
        TS(rest, (b.take(c) :+ 'a') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action("x") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b patch(from = c, other = Nil, replaced = 1), c)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'x') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action("r") {
      case TermState(char ~: rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b patch(from = c, other = Seq(char.toChar), replaced = 1), c)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'r') ++ b.drop(c), c + 1)
    }
    ,

    Filter.action("dd") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b.take(0), 0)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'd') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action(Seq("dw")) {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right: (Vector[Char], Int) = cutPasteFilter.cutWordRight(b, c)
        TS(rest, right._1, right._2)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'd') ++ b.drop(c), c + 1)
    }
    ,
    Filter.action(Seq("cw")) {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        VISUAL_MODE = false
        val right: (Vector[Char], Int) = cutPasteFilter.cutWordRight(b, c)
        TS(rest, right._1, right._2)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'c') ++ b.drop(c), c + 1)
    },
    Filter.action("D") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right: (Vector[Char], Int) = cutPasteFilter.cutLineRight(b, c)
        TS(rest, right._1, right._2)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'D') ++ b.drop(c), c + 1)
    },
    Filter.action("~") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val t = b.lift(c) match {
          case Some(j) if j.isUpper => Seq(j.toLower)
          case Some(j) => Seq(j.toUpper)
          case None => Seq()
        }
        TS(rest, b patch(from = c, other = t, replaced = 1), c + 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ '~') ++ b.drop(c), c + 1)
    },
  )

  def viNavFilter: Filter = Filter.merge(
    Filter.action("h") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, c - 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'h') ++ b.drop(c), c + 1)
    },
    Filter.action("l") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, c + 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'l') ++ b.drop(c), c + 1)
    },
    Filter.action(" ") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, c + 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ ' ') ++ b.drop(c), c + 1)
    },
    Filter.action("0") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, 0)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ '0') ++ b.drop(c), c + 1)
    },
    Filter.action("$") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, b.size)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ '$') ++ b.drop(c), c + 1)
    },
    Filter.action("b") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val left = GUILikeFilters.wordLeft(b, c)
        TS(rest, left._1, left._2)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'b') ++ b.drop(c), c + 1)
    },
    Filter.action("w") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right = GUILikeFilters.wordRight(b, c)
        TS(rest, right._1, right._2 + 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'w') ++ b.drop(c), c + 1)
    },
    Filter.action("e") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        val right = GUILikeFilters.wordRight(b, c)
        TS(rest, right._1, right._2 - 1)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ 'e') ++ b.drop(c), c + 1)
    },
    Filter.action(":") {
      case TermState(rest, b, c, _) if VISUAL_MODE =>
        TS(rest, b, b.size)
      case TermState(rest, b, c, _) =>
        TS(rest, (b.take(c) :+ ':') ++ b.drop(c), c + 1)
    }
  )


  case class ViHistoryFilter(history: () => IndexedSeq[String], comment: fansi.Attrs) extends DelegateFilter {
    val historyFilter = new HistoryFilter(history, comment)
    var oldHistoryLength: Int = history().length


    def enterFilter: Filter = action(SpecialKeys.NewLine){
      case TS(rest, b, c, _) => {
        VISUAL_MODE = false
        historyFilter.endHistory()
        doEnter(b, c, rest)
      } // Enter
    }

    override def filter: Filter = Filter.merge(
      Filter.action("k") {
        case ts if VISUAL_MODE =>
          historyFilter.wrap(ts.inputs, historyFilter.up(ts.buffer, ts.cursor))
        case TermState(rest, b, c, _) =>
          TS(rest, (b.take(c) :+ 'k') ++ b.drop(c), c + 1)
      },
      Filter.action("j") {
        case ts if VISUAL_MODE =>
          historyFilter.wrap(ts.inputs, historyFilter.down(ts.buffer, ts.cursor))
        case TermState(rest, b, c, _) =>
          TS(rest, (b.take(c) :+ 'j') ++ b.drop(c), c + 1)
      },
      enterFilter,
      partial {
        case TS(char ~: rest, b, c, _) if VISUAL_MODE => {
          TS(rest, b, c)
        }
      }
    )
  }

}
