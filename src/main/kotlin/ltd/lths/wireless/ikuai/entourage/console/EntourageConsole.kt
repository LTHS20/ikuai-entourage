package ltd.lths.wireless.ikuai.entourage.console

import ltd.lths.wireless.ikuai.entourage.Entourage
import ltd.lths.wireless.ikuai.entourage.Entourage.logger
import ltd.lths.wireless.ikuai.entourage.command.CommandManager
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.apache.logging.log4j.LogManager
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import taboolib.common.TabooLibCommon
import taboolib.common.platform.ProxyCommandSender
import java.io.PrintStream

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
 *
 * @author Score2
 * @since 2022/06/08 13:37
 */
object EntourageConsole : SimpleTerminalConsole(), ProxyCommandSender {
    override fun isRunning(): Boolean {
        return Entourage.running
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader {
        builder.completer { reader, line, candidates ->
            val buffer = line.line()
        }

        return super.buildReader(builder)
    }

    override fun runCommand(command: String) {
        CommandManager.runCommand(command)
    }

    override fun shutdown() {
        Entourage.onDisable()
    }

    override var isOp = true
    override val name = "CONSOLE"
    override val origin: Any = this

    override fun hasPermission(permission: String): Boolean {
        return true
    }

    override fun isOnline(): Boolean {
        return true
    }

    override fun performCommand(command: String): Boolean {
        CommandManager.runCommand(command)
        return true
    }

    override fun sendMessage(message: String) {
        logger.info(message)
    }
}