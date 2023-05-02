package ltd.lths.wireless.ikuai.entourage.plugin

import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.PluginLogger
 *
 * @author Score2
 * @since 2022/07/18 21:25
 */
class PluginLogger private constructor(plugin: EntouragePlugin) : Logger(plugin.name, null) {

    companion object {

        fun getLogger(plugin: EntouragePlugin): Logger {
            var logger: Logger = PluginLogger(plugin)
            if (!LogManager.getLogManager().addLogger(logger)) {
                logger = LogManager.getLogManager().getLogger(plugin.name)
            }
            return logger
        }

    }

}