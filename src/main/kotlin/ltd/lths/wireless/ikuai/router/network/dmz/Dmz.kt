package ltd.lths.wireless.ikuai.router.network.dmz

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.entourage.api.asWrittenBool
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.Ethernet

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.dmz.Dmz
 *
 * @author Score2
 * @since 2022/06/13 22:13
 */
class Dmz(val router: IkuaiRouter, val json: JsonObject) {

    val id: Int
        get() = json.get("id").asInt

    val enabled: Boolean
        get() = json.get("enabled").asWrittenBool

    val comment: String
        get() = json.get("comment").asString

    val lanAddress: String
        get() = json.get("lan_addr").asString

    val interfaces: List<String>
        get() {
            if (json.get("interface").asString == "all") {
                return router.lanWanSettings.wanInterfaces
            }
            return json.get("interface").asString.split(",")
        }


    override fun equals(other: Any?): Boolean {
        if (other !is Dmz) {
            return super.equals(other)
        }
        return other.interfaces.containsAll(interfaces)
                || other.lanAddress == lanAddress
                || other.comment == comment
    }

    enum class Protocol {

    }
}