package mwittmann.example

import mwittmann.example.withstacktrace.Parser.Result

// The stacktrace; consists of a message and a list/stack of StacktraceElements (containing file and line nr)
case class Stacktrace(text: String, trace: List[StacktraceElement]) {
  def render: String = text + "\n" + trace.map(e => s"${e.file}-${e.line}").mkString("\n")
}

// File + line number
case class StacktraceElement(file: String, line: Int)

// Stuff for creating and updating stacktraces
object Stacktrace {

  // Update an existing trace by popping on current line and file
  def updateTrace[S](in: Result[S])(implicit line: sourcecode.Line, file: sourcecode.File): Result[S] =
    in.left.map(Stacktrace.updateTrace)

  def updateTrace(trace: Stacktrace)(implicit line: sourcecode.Line, file: sourcecode.File): Stacktrace = {
    trace.copy(trace = StacktraceElement(file.value, line.value) :: trace.trace)
  }

  // Make a new trace, includes first stacktrace element as well as the message
  def makeTrace(text: String)(implicit line: sourcecode.Line, file: sourcecode.File): Stacktrace =
    Stacktrace(text, List(StacktraceElement(file.value, line.value)))

  // Same as makeTrace, but wraps in Left for convenience
  def makeLeftTrace[S](text: String)(implicit line: sourcecode.Line, file: sourcecode.File): Either[Stacktrace, S] =
    Left(makeTrace(text))

}
