package ltd.lths.wireless.ikuai.entourage.plugin

import ltd.lths.wireless.ikuai.entourage.Entourage
import ltd.lths.wireless.ikuai.entourage.Entourage.logger
import ltd.lths.wireless.ikuai.entourage.api.println
import taboolib.common.env.ClassAppender
import taboolib.common.env.RuntimeEnv
import taboolib.common.io.getClasses
import taboolib.common.io.runningClasses
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.math.log

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.PluginManager
 *
 * @author Score2
 * @since 2022/06/11 13:11
 */
object PluginManager {

    val plugins = mutableListOf<EntouragePlugin>()

    val pluginsDir = File("plugins")

    fun loadPlugins() {
        pluginsDir.mkdirs()
        pluginsDir.listFiles()?.forEach {
            if (it.name.endsWith(".jar")) {
                loadPlugin(it)
            }
        }
    }

    fun loadPlugin(file: File) {
/*        val loader = URLClassLoader(file.name, arrayOf(file.toURI().toURL()), Entourage::class.java.classLoader)
        val ucp = ClassAppender::class.java.invokeMethod<Field>("ucp", loader::class.java, fixed = true)!!
        ClassAppender::class.java.invokeMethod<Void>("addURL", loader, ucp, File(file.toPath().toUri().path), fixed = true)

        val dirs = loader.getResources("").toList()
        dirs.forEach {
            it.println()
        }*/
        ClassAppender.addPath(file.toPath())
        val jarFile = JarFile(file)
        kotlin.runCatching {
            jarFile.entries().iterator().forEach {
                if (!it.name.endsWith(".class") || it.name.contains("$")) {
                    return@forEach
                }
                val path = it.name.removeSuffix(".class").replace("/", ".")
                val clazz = Entourage::class.java.classLoader.loadClass(path)
                if (Modifier.isAbstract(clazz.modifiers)) return@forEach
                if (clazz.superclass != EntouragePlugin::class.java) return@forEach

                logger.info("正在载入插件 ${file.name}")
                RuntimeEnv.ENV.inject(clazz)
                val instance = clazz.getProperty<EntouragePlugin>("INSTANCE", true)!!
                    /*clazz
                    .asSubclass(EntouragePlugin::class.java)
                    .getConstructor()
                    .newInstance()*/



                instance.file = file
                instance.onLoad()

                plugins.add(instance)
                return@runCatching
            }
        }.onFailure {
            logger.info("载入插件 ${file.name} 遇到了错误")
            logger.info(it.stackTraceToString())
        }
    }

    fun enablePlugins() {
        plugins.forEach { plugin ->
            kotlin.runCatching {
                plugin.onEnable()
            }.onFailure {
                logger.info("启用插件 ${plugin.name} 遇到了错误")
                logger.info(it.stackTraceToString())
                return@forEach
            }

            logger.info("插件 ${plugin.name} 已启用.")
        }
    }

    fun disablePlugins() {
        plugins.forEach { plugin ->
            kotlin.runCatching {
                plugin.onDisable()
            }.onFailure {
                logger.info(it.stackTraceToString())
                return@forEach
            }
            logger.info("插件 ${plugin.name} 已卸载.")
        }
    }


}