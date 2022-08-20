package ltd.lths.wireless.ikuai.entourage.plugin.ext

import ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin
import taboolib.common.platform.function.submit

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.ext.NetworkDectect
 *
 * @author Score2
 * @since 2022/08/20 20:03
 */
object NetworkDetect : EntouragePlugin("network-detect") {

    val backupLines get() = config.getStringList("backup-lines")

    override fun onEnable() {

        Thread {


//            Thread.sleep()
        }.start()
    }

    override fun onReload() {



    }

}