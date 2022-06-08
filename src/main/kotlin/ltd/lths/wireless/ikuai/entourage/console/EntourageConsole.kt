package ltd.lths.wireless.ikuai.entourage.console

import ltd.lths.wireless.ikuai.entourage.Entourage
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.apache.logging.log4j.LogManager
import java.io.PrintStream

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
 *
 * @author Score2
 * @since 2022/06/08 13:37
 */
object EntourageConsole : SimpleTerminalConsole() {
    override fun isRunning(): Boolean {
        return Entourage.running
    }

    override fun runCommand(command: String?) {

    }

    override fun shutdown() {
        Entourage.onDisable()
    }
}