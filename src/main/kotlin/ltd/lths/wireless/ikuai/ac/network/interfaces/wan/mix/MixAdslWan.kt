package ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixAdslWan
 *
 * @author Score2
 * @since 2022/06/08 20:39
 */
class MixAdslWan(id: Int, ac: IkuaiAC) : AdslWan(id, ac) {

    override val json get() = super.json.run {
        ac.postJson(JsonObject().run {
            addProperty("func_name", "wan")
            addProperty("action", "show")
            add("param", JsonObject().run {
                addProperty("ORDER", "desc")
                addProperty("ORDER_BY", "id")
                addProperty("TYPE", "vlan_data,vlan_total")
                addProperty("interface", "wan$id")
                addProperty("limit", "0,20")
                addProperty("vlan_internet", 0)
                this
            })
            this
        }).entrySet().forEach { (key, value) ->
            add(key, value)
        }
        this
    }

    var vlanId: Int
        get() = json.get("vlan_id").asInt
        set(value) = json.addProperty("vlan_id", value)

    var vlanName: String
        get() = json.get("vlan_name").asString
        set(value) = json.addProperty("vlan_name", value)

}