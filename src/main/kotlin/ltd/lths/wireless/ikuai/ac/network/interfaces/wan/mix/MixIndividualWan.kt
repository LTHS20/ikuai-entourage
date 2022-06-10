package ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix

import com.google.gson.JsonObject

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixWanIndividual
 *
 * @author Score2
 * @since 2022/06/10 23:26
 */
interface MixIndividualWan {

    var json: JsonObject

    val id: Int
        get() = json.get("id").asInt

    var vlanId: Int
        get() = json.get("vlan_id").asInt
        set(value) = json.addProperty("vlan_id", value)

    var vlanName: String
        get() = json.get("vlan_name").asString
        set(value) = json.addProperty("vlan_name", value)
}