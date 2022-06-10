package ltd.lths.wireless.ikuai.entourage

import com.google.gson.JsonObject
import joptsimple.OptionSet
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
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
        val ac = IkuaiAC("172.18.0.1", "test", "lthstester123")
        logger.info(ac.postJson(JsonObject().run {
            addProperty("func_name", "wan")
            addProperty("action", "show")
            add("param", JsonObject().run {
                addProperty("ORDER", "desc")
                addProperty("ORDER_BY", "id")
                addProperty("TYPE", "vlan_data,vlan_total")
                addProperty("interface", "wan1")
                addProperty("limit", "0,20")
                addProperty("vlan_internet", 0)
                this
            })
            this
        }))
    }

}