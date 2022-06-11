package ltd.lths.wireless.ikuai.entourage.plugin.ext.otcdmz

import ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.ext.otcdmz.OtcDmz
 *
 * @author Score2
 * @since 2022/06/12 1:48
 */
object OtcDmz : EntouragePlugin("otc-dmz") {

    var running = false

    val otcList get() = config.getStringList("otc-list")

    override fun onEnable() {

    }

    fun start() {
        if (running) {
            return
        }
        running = true
    }

}