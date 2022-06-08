package ltd.lths.wireless.ikuai.entourage.util

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.util.ChatColor
 *
 * @author Score2
 * @since 2022/06/08 1:08
 */
enum class ChatColor(val id: String, val ansi: String) {
    BOLD("l", "[1m"),
    OBFUSCATED("k", "[5m"),
    ITALIC("o", "[3m"),
    UNDERLINE("n", "[4m"),
    STRIKETHROUGH("m", "[9m"),
    RESET("r", "[0m"),
    BLACK("0", "[0;30m"),
    DARK_BLUE("1", "[0;34m"),
    DARK_GREEN("2", "[0;32m"),
    DARK_AQUA("3", "[0;36m"),
    DARK_RED("4", "[0;31m"),
    DARK_PURPLE("5", "[0;35m"),
    GOLD("6", "[0;33m"),
    GRAY("7", "[0;37m"),
    DARK_GRAY("8", "[30;1m"),
    BLUE("9", "[34;1m"),
    GREEN("a", "[32;1m"),
    AQUA("b", "[36;1m"),
    RED("c", "[31;1m"),
    LIGHT_PURPLE("d", "[35;1m"),
    YELLOW("e", "[33;1m"),
    WHITE("f", "[37;1m"),

    ;

    val overall get() =  ESCAPE + id

    companion object {
        const val ESCAPE = '§'


        /**
         * Convert chat colour codes to terminal colours
         *
         * @param string The text to replace colours for
         *
         * @return A string ready for terminal printing
         */
        fun toANSI(string: String): String {
            var string = string
            values().forEach {
                string = string.replace(it.overall, 0x1b.toChar().toString() + it.ansi)
            }
            string += 0x1b.toChar().toString() + RESET.ansi
            return string
        }

        fun translateAlternateColorCodes(color: Char, message: String): String? {
            return message.replace(color, ESCAPE)
        }

        /**
         * Remove all colour formatting tags from a message
         *
         * @param message Message to remove colour tags from
         *
         * @return The sanitised message
         */
        fun stripColors(message: String): String? {
            var message = message
            return message.replace("(&([a-fk-or0-9]))".toRegex(), "").replace("(§([a-fk-or0-9]))".toRegex(), "")
                .replace("s/\\x1b\\[[0-9;]*[a-zA-Z]//g".toRegex(), "").also {
                    message = it
                }
        }
    }
}