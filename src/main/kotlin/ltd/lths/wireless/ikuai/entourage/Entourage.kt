package ltd.lths.wireless.ikuai.entourage

import joptsimple.OptionSet
import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.api.println
import ltd.lths.wireless.ikuai.entourage.command.CommandManager
import ltd.lths.wireless.ikuai.entourage.console.EntourageConsole
import ltd.lths.wireless.ikuai.entourage.plugin.PluginManager
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.io.IoBuilder
import taboolib.common.TabooLibCommon
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.command
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import kotlin.system.exitProcess

/**
 * iKuai-multiwan-tasks
 * ltd.lths.wireless.ikuai.router.Main
 *
 * @author Score2
 * @since 2022/05/02 15:40
 */
object Entourage {

    val logger = LogManager.getLogger(Entourage::class.java)

    @Config(autoReload = true)
    lateinit var config: Configuration

    var running = false
        private set

    val bindRouters = mutableListOf<IkuaiRouter>()

    fun main(options: OptionSet?) {
        if (options != null) when {
            options.has("test") -> {
                test()
                return
            }
        }
        running = true

        logger.info("正在初始化...")

        Runtime.getRuntime().addShutdownHook(Thread {
            onDisable()
        })

        System.setOut(IoBuilder.forLogger(logger).setLevel(Level.INFO).buildPrintStream())
        System.setErr(IoBuilder.forLogger(logger).setLevel(Level.WARN).buildPrintStream())

        println("§3LOGGER 已初始化")

        logger.info("正在载入配置文件...")
        config.onReload {
            onReload()
        }


        logger.info("正在载入iKuaiRouter...")
        loadRouters()

        logger.info("正在载入内部命令...")
        command("help", aliases = listOf("?"), description = "查看帮助信息") {
            execute<ProxyCommandSender> { sender, context, argument ->
                sender.sendMessage("§7共有命令 §f${CommandManager.commands.size} §7条, 如下:")
                CommandManager.commands.forEach {
                    sender.sendMessage("§b✦ §f${it.command.name}§7<${it.command.aliases.joinToString()}> §8- §3${it.command.description}")
                }
            }
        }
        command("stop", aliases = listOf("shutdown"), description = "停止服务器") {
            execute<ProxyCommandSender> { sender, context, argument ->
                onDisable()
            }
        }
        command("reload", aliases = listOf("rl"), description = "重新载入配置文件") {
            execute<ProxyCommandSender> { sender, context, argument ->
                onReload()
            }
        }
        command("ikuai", aliases = listOf("entourage", "router"), description = "ikuai 相关的命令") {

            dynamic {
                suggestion<ProxyCommandSender> { sender, context ->
                    bindRouters.map { it.name }
                }

                literal("ethernet", "ethernets") {
                    dynamic {
                        suggestion<ProxyCommandSender> { sender, context ->
                            val router = bindRouters.find { it.name == context.argument(-2) }!!
                            router.lanWanSettings.ethernets.map { it.name }
                        }
                    }
                }
            }
        }

        command("plugins", aliases = listOf("pl", "plugin")) {
            dynamic(optional = true) {
                literal("reload") {
                    execute<ProxyCommandSender> { sender, context, argument ->
                        val plugin = PluginManager.plugins.find { it.name == context.argument(-1) }!!
                        plugin.config.reload()
                        plugin.onReload()
                        sender.sendMessage("插件 ${plugin.name} 重新载入成功.")
                    }
                }

                suggestion<ProxyCommandSender> { sender, context ->
                    PluginManager.plugins.map { it.name }
                }
            }

            execute<ProxyCommandSender> { sender, context, argument ->
                sender.sendMessage("已载入的插件§f(${PluginManager.plugins.size})§7: §b${PluginManager.plugins.joinToString("§7, §b") { it.name }}")
            }
        }

        logger.info("正在载入插件...")
        PluginManager.loadPlugins()
        PluginManager.enablePlugins()


        logger.info("载入完成! 输入 ? 或 help 查看帮助.")

        object : Thread("console handler") {
            override fun run() {
                EntourageConsole.start()
            }
        }.run {
            isDaemon = true
            start()
        }
    }

    fun onDisable() {
        if (!running) {
            return
        }
        running = false
        PluginManager.disablePlugins()
        logger.info("停止服务器")
        TabooLibCommon.testCancel()
        exitProcess(0)
    }

    fun onReload() {
        loadRouters()
    }

    fun loadRouters() {
        config.getConfigurationSection("ikuai-router")!!.let { section ->
            bindRouters.losslessUpdate(
                section.getKeys(false).map {
                    val router = IkuaiRouter(it, section.getConfigurationSection(it)!!)
                    router
                },
                accord = { t, t1 ->
                    t.name == t1.name
                },
                adding = { t ->
                    if (t.cookie == "password") {
                        logger.info("§ciKuaiRouter ${t.name} 的密码错误, Host: ${t.ip}:${t.port}, User: ${t.username}")
                        return@losslessUpdate false
                    }
                    logger.info("已载入新 iKuaiRouter ${t.name}, Host: ${t.ip}:${t.port}, User: ${t.username}")
                    true
                },
                removing = { t ->
                    logger.info("已删除 iKuaiRouter ${t.name}, Host: ${t.ip}:${t.port}")
                    true
                },
                keepers = { t, t1 ->
                    if (t.cookie == "password") {
                        logger.info("§ciKuaiRouter ${t.name} 的密码错误, Host: ${t.ip}:${t.port}, User: ${t.username}")
                        return@losslessUpdate false
                    }
                    t.ip = t1.ip
                    t.port = t1.port
                    t.username = t1.username
                    t.password = t1.password
                    t.httpType = t1.httpType
                    logger.info("已修改原 iKuaiRouter ${t.name} 的信息, Host: ${t.ip}:${t.port}, User: ${t.username}")
                    true
                }
            )

        }
    }

    fun test() {
        logger.info("开始调试")
        val router = IkuaiRouter("test1", "172.18.0.1", "test", "lthstester123")

        /*router.lanWanSettings.getWan(1, MixWan::class.java).AdslWans.forEach {
            "${it.vlanId}:${it.vlanName}:${it.ip}:${it.username}:${it.password}".println()
        }*/
        router.lanWanSettings.ethernets.forEach {
            it.name.println()
        }

        logger.info("结束调试")
        onDisable()
    }

}