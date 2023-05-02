package ltd.lths.wireless.ikuai.router.network.interfaces

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.entourage.api.asNumBool

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.interfaces.Ethernet
 *
 * @author Score2
 * @since 2022/06/08 17:49
 */
data class Ethernet(
    val router: IkuaiRouter,
    val name: String,
    var json: JsonObject
) {

    val id = name.substringAfter("eth").toIntOrNull() ?: 0

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
