package ltd.lths.wireless.ikuai.entourage.command

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBuilder
import taboolib.common.platform.command.CommandCompleter
import taboolib.common.platform.command.CommandExecutor
import taboolib.common.platform.command.CommandStructure
import taboolib.common.platform.service.PlatformCommand

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.command.TabooCommandAdapt
 *
 * @author Score2
 * @since 2022/06/08 0:26
 */
class TabooCommandAdapt : PlatformCommand {

    override fun registerCommand(
        command: CommandStructure,
        executor: CommandExecutor,
        completer: CommandCompleter,
        commandBuilder: CommandBuilder.CommandBase.() -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun unknownCommand(sender: ProxyCommandSender, command: String, state: Int) {
        TODO("Not yet implemented")
    }

    override fun unregisterCommand(command: String) {
        TODO("Not yet implemented")
    }

    override fun unregisterCommands() {
        TODO("Not yet implemented")
    }

}