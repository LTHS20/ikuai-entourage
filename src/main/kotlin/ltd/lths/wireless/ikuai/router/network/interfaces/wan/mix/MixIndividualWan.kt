package ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.entourage.api.asWrittenBool
import ltd.lths.wireless.ikuai.entourage.api.asWrittenString
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.WanInterface

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixWanIndividual
 *
 * @author Score2
 * @since 2022/06/10 23:26
 */
interface MixIndividualWan : WanInterface {

    var json: JsonObject

    val id: Int
        get() = json.get("id").asInt

    var vlanId: Int
        get() = json.get("vlan_id").asInt
        set(value) = json.addProperty("vlan_id", value)

    var vlanName: String
        get() = json.get("vlan_name").asString
        set(value) = json.addProperty("vlan_name", value)

    override val interfaceName: String
        get() = vlanName


    var isEnabled: Boolean
        get() = json.get("enabled").asWrittenBool
        set(value) = json.addProperty("enabled", value.asWrittenString)
}