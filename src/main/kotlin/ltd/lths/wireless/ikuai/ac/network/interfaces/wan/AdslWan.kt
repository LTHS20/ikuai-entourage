package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.api.asNumBool
import ltd.lths.wireless.ikuai.entourage.api.asNumSign
import ltd.lths.wireless.ikuai.entourage.api.asWrittenBool
import ltd.lths.wireless.ikuai.entourage.api.asWrittenString
import ltd.lths.wireless.ikuai.entourage.util.Persistent

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
open class AdslWan(id: Int, ac: IkuaiAC): Wan(id, ac) {

    var isEnabled: Boolean
        get() = json.get("enabled").asWrittenBool
        set(value) = json.addProperty("enabled", value.asWrittenString)

    var isDefaultGateway: Boolean
        get() = json.get("default_route").asNumBool
        set(value) = json.addProperty("default_route", value.asNumSign)

    var username: String
        get() = json.get("username").asString
        set(value) = json.addProperty("username", value)

    var password: String
        get() = json.get("passwd").asString
        set(value) = json.addProperty("passwd", value)


}