package ltd.lths.wireless.ikuai.entourage

import ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
import net.minecrell.terminalconsole.TerminalConsoleAppender
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.core.appender.ConsoleAppender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit

/**
 * iKuai-multiwan-tasks
 * ltd.lths.wireless.ikuai.ac.Main
 *
 * @author Score2
 * @since 2022/05/02 15:40
 */
object Entourage {

    val logger = LogManager.getLogger(Entourage::class.java)

    var running = false
        private set

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        running = true
        object : Thread("console handler") {
            override fun run() {
                EntourageConsole.start()
            }
        }.run {
            isDaemon = true
            start()
        }

        logger.info("正在初始化...")

        Runtime.getRuntime().addShutdownHook(Thread {
            onDisable()
        })

    }

    fun onDisable() {
        running = false
        logger.info("停止")
    }

}