package ltd.lths.wireless.ikuai.entourage.command

import taboolib.common.platform.command.CommandCompleter
import taboolib.common.platform.command.CommandExecutor
import taboolib.common.platform.command.CommandStructure
import taboolib.common.platform.command.component.CommandBase

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.command.Command
 *
 * @author Score2
 * @since 2022/06/08 0:18
 */
data class Command(
    val command: CommandStructure,
    val executor: CommandExecutor,
    val completer: CommandCompleter,
    val commandBuilder: CommandBase.() -> Unit
) {

    val aliases get() = listOf(command.name, *command.aliases.toTypedArray())

    fun register() =
        CommandManager.register(this)

    fun unregister() =
        CommandManager.unregister(this)

}