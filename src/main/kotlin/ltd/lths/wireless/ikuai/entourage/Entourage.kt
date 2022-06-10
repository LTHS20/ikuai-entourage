package ltd.lths.wireless.ikuai.entourage

import com.google.gson.JsonObject
import joptsimple.OptionSet
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.MixWan
import ltd.lths.wireless.ikuai.entourage.api.println
import ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
import ltd.lths.wireless.ikuai.entourage.util.ActionProp
import org.apache.logging.log4j.LogManager
import taboolib.common.TabooLibCommon
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * iKuai-multiwan-tasks
 * ltd.lths.wireless.ikuai.ac.Main
 *
 * @author Score2
 * @since 2022/05/02 15:40
 */
object Entourage {

    val logger = LogManager.getLogger(Entourage::class.java)

    @Config
    lateinit var config: Configuration

    var running = false
        private set

    fun main(options: OptionSet?) {
        if (options != null) when {
            options.has("test") -> {
                test()
                return
            }
        }
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

        logger.info("载入完成! 输入 ? 或 help 查看帮助.")
    }

    fun onDisable() {
        running = false
        logger.info("停止")
        TabooLibCommon.testCancel()
    }

    fun test() {
        logger.info("开始调试")
        val ac = IkuaiAC("172.18.0.1", "test", "lthstester123")

        ac.lanWanSettings.getWan(1, MixWan::class.java).AdslWans.forEach {
            "${it.vlanId}:${it.vlanName}:${it.ip}:${it.username}:${it.password}".println()
        }

        logger.info("结束调试")
        onDisable()
    }

}