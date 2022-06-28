package ltd.lths.wireless.ikuai.entourage.plugin.ext.dmzbinder

import ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.ext.otcdmz.OtcDmz
 *
 * 点对点广域网ip独享dmz
 *
 * @author Score2
 * @since 2022/06/12 1:48
 */
object DmzBinder : EntouragePlugin("dmz-binder") {

    var running = false

    val otcList get() = config.getStringList("dmz-list")

    override fun onEnable() {

    }

    fun start() {
        if (running) {
            return
        }
        running = true
    }

}