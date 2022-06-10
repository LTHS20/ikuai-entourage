package ltd.lths.wireless.ikuai.ac.network.interfaces

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.lan.Lan
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.*
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.Wan.Type.*
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

    val wans get() = json.getAsJsonObject("Data").getAsJsonArray("snapshoot_wan").map {
        val jsonObject = it.asJsonObject
        val id = jsonObject.get("id").asInt

        when (Wan.Type.find(jsonObject.get("internet").asInt)) {
            STATIC -> RootStaticWan(id, ac)
            DYNAMIC -> RootDynamicWan(id, ac)
            ADSL -> RootAdslWan(id, ac)
            MIX, VLAN_MIX -> MixWan(id, ac)
        }
    }

    val lans get() = json.getAsJsonObject("Data").getAsJsonArray("snapshoot_lan").map {
        val jsonObject = it.asJsonObject
        val id = jsonObject.get("id").asInt

        when (Wan.Type.find(jsonObject.get("internet").asInt)) {
            STATIC -> RootStaticWan(id, ac)
            DYNAMIC -> RootDynamicWan(id, ac)
            ADSL -> RootAdslWan(id, ac)
            MIX, VLAN_MIX -> MixWan(id, ac)
        }
    }


    fun getWan(id: Int) =
        getWan(id, Wan::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T> getWan(id: Int, type: Class<T>) =
        wans.find { it.wanId == id } as T

    fun getLan(id: Int) =
        lans.find { it.wanId == id }

}