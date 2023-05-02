package ltd.lths.wireless.ikuai.entourage.command

import ltd.lths.wireless.ikuai.entourage.Entourage.logger
import ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.service.PlatformCommand

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.command.EntourageCommandManager
 *
 * @author Score2
 * @since 2022/06/07 23:43
 */
@Awake
class CommandManager : PlatformCommand {

    companion object {

        const val unknownCommandMessage = "未知命令, 输入 help 或 ? 查看帮助"

        val commands = mutableSetOf<Command>()

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
            val command = commands.find { it.aliases.contains(label) } ?: return logger.info(unknownCommandMessage)
            val args = if (content.contains(" ")) content.substringAfter(" ").split(" ") else listOf()

            command.executor.execute(EntourageConsole, command.command, label, args.toTypedArray())
        }

        fun suggest(content: String): List<String> {
            fun suggestion() = commands.flatMap { it.aliases }

            if (content.isBlank()) {
                return suggestion()
            }
            val label = if (content.contains(" ")) content.substringBefore(" ") else content

            val command = commands.find { it.aliases.contains(label) } ?: return suggestion().filter { it.startsWith(label) }

            return if (content.contains(" ")) {
                command.completer.execute(EntourageConsole, command.command, label, content.substringAfter(" ").split(" ").toTypedArray()) ?: listOf()
            } else {
                listOf()
            }
        }
    }

    override fun registerCommand(
        command: CommandStructure,
        executor: CommandExecutor,
        completer: CommandCompleter,
        commandBuilder: CommandBase.() -> Unit
    ) {
        register(Command(command, executor, completer, commandBuilder))
    }

    override fun unknownCommand(sender: ProxyCommandSender, command: String, state: Int) {
        sender.sendMessage("§7$command§r§c§o<--[HERE]")
    }

    override fun unregisterCommand(label: String) {
        unregister(commands.find { it.command.aliases.contains(label) } ?: return)
    }

    override fun unregisterCommands() {
        commands.forEach {
            unregister(it)
        }
    }

}