package ltd.lths.wireless.ikuai.ac.network.interfaces

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.api.asNumBool
import ltd.lths.wireless.ikuai.entourage.api.asNumSign

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.Ethernet
 *
 * @author Score2
 * @since 2022/06/08 17:49
 */
data class Ethernet(
    val ac: IkuaiAC,
    val id: Int,
    var json: JsonObject
) {

    val name = "eth$id"

    val driver: String
        get() = json.get("driver").asString

    val type: String
        get() = json.get("type").asString

    val mac: String
        get() = json.get("mac").asString

    val linked: Boolean
        get() = json.get("link").asNumBool

    val speed: Int
        get() = json.get("speed").asInt

    val isDuplex: Boolean
        get() = json.get("duplex").asNumBool

    val model: String
        get() = json.get("model").asString

    val `interface`: String
        get() = json.get("interface").asString

    val isLock: Boolean
        get() = json.get("lock").asNumBool

}
