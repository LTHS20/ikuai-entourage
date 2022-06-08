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


    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        val rootLogger = LogManager.getRootLogger() as Logger
        for (appender in rootLogger.appenders.values) {
            // Remove the appender that is not in use
            // Prevents multiple appenders/double logging and removes harmless errors
            if (appender is ConsoleAppender) {
                rootLogger.removeAppender(appender)
            }
        }
        val thread = object : Thread("console handler") {
            override fun run() {
                EntourageConsole.start()
            }
        }
        thread.isDaemon = true

        thread.start()
        submit {
            initServer()
        }
    }

    fun initServer() {
        println("正常输出")
        logger.info("§b测试文字")
    }

}