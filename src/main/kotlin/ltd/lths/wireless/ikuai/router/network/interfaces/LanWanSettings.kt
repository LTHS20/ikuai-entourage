package ltd.lths.wireless.ikuai.router.network.interfaces

import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.*
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.Wan.Type.*
import ltd.lths.wireless.ikuai.entourage.util.ActionProp

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.`interface`.IkuaiLanWanSettings
 *
 * @author Score2
 * @since 2022/06/08 17:46
 */
class LanWanSettings(val router: IkuaiRouter) {

    val json get() = ActionProp.actionShowEtherInfo(router).postJson(router)

    val ethernets get() =
        json.getAsJsonObject("Data").getAsJsonObject("ether_info").let { infos ->
            infos.keySet().map {
                Ethernet(router, it, infos.getAsJsonObject(it))
            }
        }

    val wans get() = json.getAsJsonObject("Data").getAsJsonArray("snapshoot_wan").map {
        val jsonObject = it.asJsonObject
        val id = jsonObject.get("id").asInt

        when (Wan.Type.find(jsonObject.get("internet").asInt)) {
            STATIC -> RootStaticWan(id, router)
            DYNAMIC -> RootDynamicWan(id, router)
            ADSL -> RootAdslWan(id, router)
            MIX, VLAN_MIX -> MixWan(id, router)
        }
    }

    val wanInterfaces get() = wans.flatMap {
        if (it !is MixWan) {
            return@flatMap listOf(it.interfaceName)
        }
        it.individualWans.map { it.interfaceName }
    }

    val lans get() = json.getAsJsonObject("Data").getAsJsonArray("snapshoot_lan").map {
        val jsonObject = it.asJsonObject
        val id = jsonObject.get("id").asInt

        when (Wan.Type.find(jsonObject.get("internet").asInt)) {
            STATIC -> RootStaticWan(id, router)
            DYNAMIC -> RootDynamicWan(id, router)
            ADSL -> RootAdslWan(id, router)
            MIX, VLAN_MIX -> MixWan(id, router)
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