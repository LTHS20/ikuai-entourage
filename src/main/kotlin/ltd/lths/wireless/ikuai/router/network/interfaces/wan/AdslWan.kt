package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import com.google.gson.JsonObject

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan
 *
 * @author Score2
 * @since 2022/06/10 23:58
 */
interface AdslWan : WanInterface {

    val json: JsonObject

    var username: String
        get() = json.get("username").asString
        set(value) = json.addProperty("username", value)

    var password: String
        get() = json.get("passwd").asString
        set(value) = json.addProperty("passwd", value)

    val ip: String
        get() = json.get("pppoe_ip_addr").asString
}