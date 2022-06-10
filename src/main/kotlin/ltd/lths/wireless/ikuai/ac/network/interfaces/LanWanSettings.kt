package ltd.lths.wireless.ikuai.ac.network.interfaces

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.Wan
import ltd.lths.wireless.ikuai.entourage.util.ActionProp

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.`interface`.IkuaiLanWanSettings
 *
 * @author Score2
 * @since 2022/06/08 17:46
 */
class LanWanSettings(val ac: IkuaiAC) {

    val json get() = ActionProp.actionShowEtherInfo(ac).postJson(ac)

    val ethernets get() =
        json.getAsJsonObject("Data").getAsJsonObject("ether_info").let { infos ->
            infos.keySet().map {
                Ethernet(ac, it.substringAfter("eth").toInt(), infos.getAsJsonObject(it))
            }
        }

    val wanList get() = mutableListOf<Wan>()

}