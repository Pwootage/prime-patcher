package com.pwootage.metroidprime.utils

object Logger {
  private val ESC = "\u001b["
  val clearCurrentLine = s"${ESC}G${ESC}2K"

  def success(msg: String): Unit = {
    colored(Console.GREEN, msg)
  }

  def info(msg: String): Unit = {
    colored(Console.CYAN, msg)
  }

  def progress(msg: String): Unit = {
    colored(Console.RESET, msg)
  }

  def progressResetLine(msg: String): Unit = {
    colored(clearCurrentLine + Console.RESET, msg)
  }

  def warning(msg: String): Unit = {
    colored(Console.YELLOW, msg)
  }

  def error(msg: String): Unit = {
    colored(Console.RED, msg)
  }

  def colored(color: String, msg: String): Unit = {
    println(s"$color$msg${Console.RESET}")
  }
}
