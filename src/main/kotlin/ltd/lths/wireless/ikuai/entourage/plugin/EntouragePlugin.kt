package ltd.lths.wireless.ikuai.entourage.plugin

import taboolib.common.io.newFile
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.jar.JarFile
import java.util.logging.LogManager

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin
 *
 * @author Score2
 * @since 2022/06/11 13:11
 */
abstract class EntouragePlugin(val name: String) {

    lateinit var file: File

    val logger by lazy { LogManager.getLogManager().getLogger(name) }
    val dataFolder = File(PluginManager.pluginsDir, name)
    val jarFile by lazy { JarFile(file) }

    lateinit var config: Configuration
    private val configFile by lazy { File(dataFolder, "config.yml") }

    fun onLoad() {
        val configEntry = jarFile.getJarEntry("config.yml")
        if (configEntry != null) {
            dataFolder.mkdirs()

            if (!configFile.exists()) {
                val stream = jarFile.getInputStream(configEntry)
                newFile(configFile).writeBytes(stream.readBytes())
                stream.close()
            }

            config = Configuration.loadFromFile(configFile.also { logger.info(it.path) })
            logger.info("默认配置文件已载入")
        }
    }

    open fun onEnable() {

    }

    open fun onReload() {

    }

    open fun onDisable() {

    }

}