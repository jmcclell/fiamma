package com.tsunderebug.fiamma

import com.googlecode.lanterna
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.tsunderebug.fiamma.input.KeyStroke

object Terminal {

  def apply()(implicit terminalFactory: lanterna.terminal.TerminalFactory = new DefaultTerminalFactory): Terminal = new Terminal(terminalFactory.createTerminal())

  private[Terminal] class ResizeListeners(terminal: Terminal) {

    def +=(listener: lanterna.terminal.TerminalResizeListener): Unit = terminal.provider.addResizeListener(listener)
    def -=(listener: lanterna.terminal.TerminalResizeListener): Unit = terminal.provider.removeResizeListener(listener)

  }

  private[Terminal] class SGRContainer(terminal: Terminal) {

    def +=(sgr: SGR): Unit = terminal.provider.enableSGR(sgr.internal)
    def -=(sgr: SGR): Unit = terminal.provider.disableSGR(sgr.internal)

  }

}

class Terminal(private[Terminal] val provider: lanterna.terminal.Terminal) {

  lazy val resizeListeners: Terminal.ResizeListeners = new Terminal.ResizeListeners(this)
  lazy val sgr: Terminal.SGRContainer = new Terminal.SGRContainer(this)

  def clear(): Unit = provider.clearScreen()
  def flush(): Unit = provider.flush()

  def input: KeyStroke = KeyStroke(provider.readInput())

  def enterPrivateMode(): Unit = provider.enterPrivateMode()
  def exitPrivateMode(): Unit = provider.exitPrivateMode()

  def bell(): Unit = provider.bell()

  def close(): Unit = provider.close()

  def cursorPosition: (Int, Int) = {
    val pos = provider.getCursorPosition
    (pos.getColumn, pos.getRow)
  }
  def cursorPosition_=(position: (Int, Int)): Unit = provider.setCursorPosition(position._1, position._2)

  // FIXME
  def putCharacter(c: Char): Unit = provider.putCharacter(c)

}
