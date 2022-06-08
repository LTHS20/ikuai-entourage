package ltd.lths.wireless.ikuai.entourage.command

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.command.EntourageCommandManager
 *
 * @author Score2
 * @since 2022/06/07 23:43
 */
object CommandManager {

    val commands = mutableSetOf<Command>()

    const val unknownCommandMessage = "未知命令, 输入 help 或 ? 查看帮助"

    fun register(command: Command) {
        commands.add(command)
    }

    fun unregister(name: String) {
        val command = commands.find { it.command.aliases.contains(name) } ?: return
        unregister(name)
    }

    fun unregister(command: Command) {
        commands.remove(command)
    }

    fun runCommand(content: String) {
        if (content.isBlank()) {
            return
        }
        val label = if (content.contains(" ")) content.substringBefore(" ") else content
        var args = if (content.contains(" ")) content.substringAfter(" ").split(" ") else listOf()

        val command = commands.find { it.command.aliases.contains(label) } ?: return


    }

    fun suggest(content: String): String {
        return ""
    }

}