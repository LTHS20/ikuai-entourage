package ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.DynamicWan
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.Wan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixDenamicWan
 *
 * @author Score2
 * @since 2022/06/08 20:38
 */
class MixDynamicWan(id: Int, ac: IkuaiAC) : DynamicWan(id, ac) {

    override val json get() = ac.postJson(JsonObject().run {
        addProperty("func_name", "wan")
        addProperty("action", "show")
        add("param", JsonObject().run {
            addProperty("ORDER", "desc")
            addProperty("ORDER_BY", "id")
            addProperty("TYPE", "vlan_data,vlan_total")
            addProperty("interface", "wan$id")
            addProperty("limit", "0,20")
            addProperty("vlan_internet", 1)
            this
        })
        this
    })

    var vlanId: Int
        get() = json.get("vlan_id").asInt
        set(value) = json.addProperty("vlan_id", value)

    var vlanName: String
        get() = json.get("vlan_name").asString
        set(value) = json.addProperty("vlan_name", value)
}