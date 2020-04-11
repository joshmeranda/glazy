package com.jmeranda.glazy.lib

import java.util.logging.*

/**
 * Formatter for handler logging.
 */
private class GlazyLoggerFormatter : Formatter() {
    override fun format(record: LogRecord?): String {
        val builder = StringBuilder()

        builder.append("${record?.level?.name}: ")
        builder.append(record?.message)
        builder.append("\n")

        return builder.toString()
    }
}

/**
 * Turn on a logger and set the formatter.
 */
fun Logger.makeVerbose() : Logger {
    val handler = ConsoleHandler()

    this.level = Level.ALL
    this.useParentHandlers = false

    handler.formatter = GlazyLoggerFormatter()
    this.addHandler(handler)

    return this
}