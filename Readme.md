# Stack traces for Monads

The one painful thing about working with Monads or Applicatives is that we lose useful  stack traces as execution is trampolined all over the place. Not that those are the only culprits, asynchronous code too loses its stack trace as its execution gets bounced all over the place.

Luckily, there's a solution: https://github.com/lihaoyi/sourcecode . This amazing little library provides line and file information (and more) via implicit, meaning we can build our own stack traces. 

This is a little example for me to get the hang of how we might implement this; the stacktrace-free version is under mwittmann.example.nostacktrace, while the stacktrace-enabled version is under mwittmann.example.withstacktrace.