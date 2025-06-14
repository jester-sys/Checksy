package com.jaixlabs.checksy.util

import com.jaixlabs.checksy.util.AppConstants.DEFAULT_ESCAPE_CHARACTER
import com.jaixlabs.checksy.util.AppConstants.DEFAULT_LINE_END
import com.jaixlabs.checksy.util.AppConstants.DEFAULT_QUOTE_CHARACTER
import com.jaixlabs.checksy.util.AppConstants.DEFAULT_SEPARATOR
import com.jaixlabs.checksy.util.AppConstants.NO_ESCAPE_CHARACTER
import com.jaixlabs.checksy.util.AppConstants.NO_QUOTE_CHARACTER

internal object CsvUtil {

    /**
     * Constructs a Csv Writer with supplied separator, quote char, escape char and line ending.
     *
     * @param writer
     * the writer to an underlying CSV source.
     * @param separator
     * the delimiter to use for separating entries
     * @param quoteChar
     * the character to use for quoted elements
     * @param escapeChar
     * the character to use for escaping quoteChars or escapeChars
     * @param lineEnd
     * the line feed terminator to use
     */
    class Writer(
        private val writer: java.io.Writer?,
        private val separator: Char = DEFAULT_SEPARATOR,
        private val quoteChar: Char = DEFAULT_QUOTE_CHARACTER,
        private val escapeChar: Char = DEFAULT_ESCAPE_CHARACTER,
        private val lineEnd: String = DEFAULT_LINE_END,
    ) {
        init {
            // write the separator to the top of the file
            writer?.write("sep=$separator\n")
        }

        /**
         * Writes the next line to the file.
         *
         * @param nextLine
         * a string array with each comma-separated element as a separate
         * entry.
         */
        fun writeNext(nextLine: Array<String>) {
            val builder = StringBuilder()
            for (i in nextLine.indices) {
                if (i != 0) {
                    builder.append(separator)
                }
                val nextElement = nextLine[i]

                if (quoteChar != NO_QUOTE_CHARACTER) {
                    builder.append(quoteChar)
                }

                for (element in nextElement) {
                    if (escapeChar == NO_ESCAPE_CHARACTER && (element == quoteChar || element == escapeChar)) {
                        builder.append(escapeChar)
                    }
                    builder.append(element)
                }

                if (quoteChar != NO_QUOTE_CHARACTER) {
                    builder.append(quoteChar)
                }
            }
            builder.append(lineEnd)
            writer?.write(builder.toString())
        }

        /**
         * Close the underlying stream writer flushing any buffered content.
         */
        fun close() = writer?.runCatching {
            flush()
            close()
        }
    }

    /**
     * Constructs a Csv Reader with supplied separator, quote char, escape char and line ending.
     *
     * @param reader
     * the writer to an underlying CSV source.
     * @param separator
     * the delimiter to use for separating entries
     * @param quoteChar
     * the character to use for quoted elements
     * @param escapeChar
     * the character to use for escaping quoteChars or escapeChars
     * @param lineEnd
     * the line feed terminator to use
     */
    class Reader(
        private val reader: java.io.Reader?,
        private val separator: Char = DEFAULT_SEPARATOR,
        private val quoteChar: Char = DEFAULT_QUOTE_CHARACTER,
        private val escapeChar: Char = DEFAULT_ESCAPE_CHARACTER,
        private val lineEnd: String = DEFAULT_LINE_END,
    ) {

        /**
         * Reads the entire file into a List with each element being a String[] of tokens.
         */
        fun rows(): List<Array<String>> {
            var lines = reader?.readLines()
            // remove the first line if it starts with "sep="
            lines = if (lines?.firstOrNull()?.startsWith("sep=") == true) {
                lines.drop(1)
            } else {
                lines
            }
            // split the lines into chunks and trim the chunks
            return lines?.map { line ->
                val tokens = line.split(separator)
                tokens.map { token ->
                    token.replace(quoteChar, ' ').replace(escapeChar, ' ').trim()
                }.toTypedArray()
            } ?: emptyList()
        }

        /**
         * Close the underlying stream reader.
         */
        fun close() = reader?.runCatching {
            close()
        }
    }
}