package ltd.lths.wireless.ikuai.entourage.console

import net.minecrell.terminalconsole.SimpleTerminalConsole

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
 *
 * @author Score2
 * @since 2022/06/08 13:37
 */
object EntourageConsole : SimpleTerminalConsole() {
    override fun isRunning(): Boolean {
        return true
    }

    override fun runCommand(command: String?) {

    }

    override fun shutdown() {

    }
}