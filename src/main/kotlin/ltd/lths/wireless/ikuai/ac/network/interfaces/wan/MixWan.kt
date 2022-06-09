package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.Wan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.MixWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
class MixWan(id: Int, ac: IkuaiAC) : Wan(id, ac) {

    var assMulti: Int
        get() = json.get("ass_multi_total").asInt
        set(value) = json.addProperty("ass_multi_total", value)

    enum class Type(val internet: Int) {
        STATIC(0),
        DYNAMIC(1),
        ADSL(2),
    }
}