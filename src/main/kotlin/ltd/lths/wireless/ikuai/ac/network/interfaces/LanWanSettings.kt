package ltd.lths.wireless.ikuai.ac.network.interfaces

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.Wan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.`interface`.IkuaiLanWanSettings
 *
 * @author Score2
 * @since 2022/06/08 17:46
 */
class LanWanSettings(val ac: IkuaiAC) {

    val json get() = ac.postJson(JsonObject().run {
        addProperty("func_name", "homepage")
        addProperty("action", "show")
        add("param", JsonObject().run {
            addProperty("TYPE", "ether_info,snapshoot")
            this
        })
        this
    })

    val ethernets get() =
        json.getAsJsonObject("Data").getAsJsonObject("ether_info").let { infos ->
            infos.keySet().map {
                Ethernet(ac, it.substringAfter("eth").toInt(), infos.getAsJsonObject(it))
            }
        }

    val wanList get() = mutableListOf<Wan>()

}