package ltd.lths.wireless.ikuai.entourage.console

import org.apache.logging.log4j.util.PerformanceSensitive
import org.apache.logging.log4j.core.LogEvent
import net.minecrell.terminalconsole.TerminalConsoleAppender
import org.apache.logging.log4j.util.PropertiesUtil
import net.minecrell.terminalconsole.MinecraftFormattingConverter
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.pattern.*
import java.lang.StringBuilder
import java.util.regex.Pattern

/**
 * Modified version of [
 * TerminalConsoleAppender's MinecraftFormattingConverter](https://github.com/Minecrell/TerminalConsoleAppender/blob/master/src/main/java/net/minecrell/terminalconsole/MinecraftFormattingConverter.java) to support hex color codes using the md_5 &x&r&r&g&g&b&b format.
 */
@Plugin(name = "entourageFormatting", category = PatternConverter.CATEGORY)
@ConverterKeys("entourageFormatting")
@PerformanceSensitive("allocation")
class HexFormattingConverter(private val formatters: List<PatternFormatter>, strip: Boolean) : LogEventPatternConverter("paperMinecraftFormatting", null) {
    private val ansi: Boolean

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to manipulate
     * @param strip      If true, the converter will strip all formatting codes
     */
    init {
        ansi = !strip
    }

    override fun format(event: LogEvent, toAppendTo: StringBuilder) {
        val start = toAppendTo.length
        var i = 0
        val size = formatters.size
        while (i < size) {
            formatters[i].format(event, toAppendTo)
            i++
        }
        if (KEEP_FORMATTING || toAppendTo.length == start) {
            // Skip replacement if disabled or if the content is empty
            return
        }
        val useAnsi = ansi && TerminalConsoleAppender.isAnsiSupported()
        var content = toAppendTo.substring(start)
        content = if (useAnsi) convertRGBColors(content) else stripRGBColors(content)
        format(content, toAppendTo, start, useAnsi)
    }

    companion object {
        private val KEEP_FORMATTING =
            PropertiesUtil.getProperties().getBooleanProperty(MinecraftFormattingConverter.KEEP_FORMATTING_PROPERTY)
        private const val ANSI_RESET = "\u001B[m"
        private const val COLOR_CHAR = '§'
        private const val LOOKUP = "0123456789abcdefklmnor"
        private const val RGB_ANSI = "\u001B[38;2;%d;%d;%dm"
        private val NAMED_PATTERN = Pattern.compile(COLOR_CHAR.toString() + "[0-9a-fk-orA-FK-OR]")
        private val RGB_PATTERN = Pattern.compile(COLOR_CHAR.toString() + "x(" + COLOR_CHAR + "[0-9a-fA-F]){6}")
        private val ansiCodes = arrayOf(
            "\u001B[0;30m",  // Black §0
            "\u001B[0;34m",  // Dark Blue §1
            "\u001B[0;32m",  // Dark Green §2
            "\u001B[0;36m",  // Dark Aqua §3
            "\u001B[0;31m",  // Dark Red §4
            "\u001B[0;35m",  // Dark Purple §5
            "\u001B[0;33m",  // Gold §6
            "\u001B[0;37m",  // Gray §7
            "\u001B[0;30;1m",  // Dark Gray §8
            "\u001B[0;34;1m",  // Blue §9
            "\u001B[0;32;1m",  // Green §a
            "\u001B[0;36;1m",  // Aqua §b
            "\u001B[0;31;1m",  // Red §c
            "\u001B[0;35;1m",  // Light Purple §d
            "\u001B[0;33;1m",  // Yellow §e
            "\u001B[0;37;1m",  // White §f
            "\u001B[5m",  // Obfuscated §k
            "\u001B[21m",  // Bold §l
            "\u001B[9m",  // Strikethrough §m
            "\u001B[4m",  // Underline §n
            "\u001B[3m",  // Italic §o
            ANSI_RESET
        )

        private fun convertRGBColors(input: String): String {
            val matcher = RGB_PATTERN.matcher(input)
            val buffer = StringBuffer()
            while (matcher.find()) {
                val s = matcher.group().replace(COLOR_CHAR.toString(), "").replace('x', '#')
                val hex = Integer.decode(s)
                val red = hex shr 16 and 0xFF
                val green = hex shr 8 and 0xFF
                val blue = hex and 0xFF
                val replacement = String.format(RGB_ANSI, red, green, blue)
                matcher.appendReplacement(buffer, replacement)
            }
            matcher.appendTail(buffer)
            return buffer.toString()
        }

        private fun stripRGBColors(input: String): String {
            val matcher = RGB_PATTERN.matcher(input)
            val buffer = StringBuffer()
            while (matcher.find()) {
                matcher.appendReplacement(buffer, "")
            }
            matcher.appendTail(buffer)
            return buffer.toString()
        }

        fun format(content: String, result: StringBuilder, start: Int, ansi: Boolean) {
            val next = content.indexOf(COLOR_CHAR)
            val last = content.length - 1
            if (next == -1 || next == last) {
                result.setLength(start)
                result.append(content)
                if (ansi) {
                    result.append(ANSI_RESET)
                }
                return
            }
            val matcher = NAMED_PATTERN.matcher(content)
            val buffer = StringBuffer()
            while (matcher.find()) {
                val format = LOOKUP.indexOf(matcher.group()[1].lowercaseChar())
                if (format != -1) {
                    matcher.appendReplacement(buffer, if (ansi) ansiCodes[format] else "")
                }
            }
            matcher.appendTail(buffer)
            result.setLength(start)
            result.append(buffer.toString())
            if (ansi) {
                result.append(ANSI_RESET)
            }
        }

        /**
         * Gets a new instance of the [HexFormattingConverter] with the
         * specified options.
         *
         * @param config  The current configuration
         * @param options The pattern options
         * @return The new instance
         *
         * @see HexFormattingConverter
         */
        fun newInstance(config: Configuration?, options: Array<String?>): HexFormattingConverter? {
            if (options.size < 1 || options.size > 2) {
                LOGGER.error("Incorrect number of options on paperMinecraftFormatting. Expected at least 1, max 2 received " + options.size)
                return null
            }
            if (options[0] == null) {
                LOGGER.error("No pattern supplied on paperMinecraftFormatting")
                return null
            }
            val parser = PatternLayout.createPatternParser(config)
            val formatters = parser.parse(options[0])
            val strip = options.size > 1 && "strip" == options[1]
            return HexFormattingConverter(formatters, strip)
        }
    }
}