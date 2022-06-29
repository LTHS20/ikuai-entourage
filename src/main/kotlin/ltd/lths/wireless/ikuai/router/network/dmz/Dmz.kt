package ltd.lths.wireless.ikuai.router.network.dmz

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.entourage.api.asIpv4Decimal
import ltd.lths.wireless.ikuai.entourage.api.asWrittenBool
import ltd.lths.wireless.ikuai.entourage.api.asWrittenString
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.Ethernet
import taboolib.common.reflect.Reflex.Companion.setProperty

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

    var enabled: Boolean
        get() = json.get("enabled").asWrittenBool
        set(value) = json.setProperty("enabled", value.asWrittenString)

    var comment: String
        get() = json.get("comment").asString
        set(value) = json.setProperty("comment", value)

    var excludePorts: String
        get() = json.get("excl_port").asString
        set(value) = json.setProperty("excl_port", value)

    var lanAddress: String
        get() = json.get("lan_addr").asString
        set(value) = json.setProperty("lan_addr", value)

    val lanAddrNum: Long
        get() = lanAddress.asIpv4Decimal

    var protocol: Protocol
        get() = Protocol.valueOf(json.get("protocol").asString)
        set(value) = json.setProperty("protocol", value.standard)

    var interfaces: List<String>
        get() {
            if (json.get("interface").asString == "all") {
                return router.lanWanSettings.wanInterfaces
            }
            return json.get("interface").asString.split(",")
        }
        set(value) {
            if (value.isEmpty()) {
                json.setProperty("interface", router.lanWanSettings.wanInterfaces.joinToString(","))
                return
            }

            json.setProperty("interface", value.joinToString(","))
        }


    override fun equals(other: Any?): Boolean {
        if (other !is Dmz) {
            return super.equals(other)
        }
        return other.interfaces.containsAll(interfaces)
                || other.lanAddress == lanAddress
                || other.comment == comment
    }

    enum class Protocol(val standard: String) {
        TCP("tcp"),
        UDP("udp"),
        TCP_UDP("tcp+udp"),
        ANY("any"),
    }
}