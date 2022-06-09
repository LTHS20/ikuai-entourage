package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.`interface`.wan.Wan
 *
 * @author Score2
 * @since 2022/06/08 17:47
 */
abstract class Wan(val id: Int, val ac: IkuaiAC) {

    open val json get() = ac.postJson(JsonObject().run {
        addProperty("func_name", "wan")
        addProperty("action", "show")
        add("param", JsonObject().run {
            addProperty("id", "$id")
            addProperty("TYPE", "support_wisp,total,data")
            this
        })
        this
    })

    enum class Type(val internet: Int) {
        STATIC(1),
        DYNAMIC(2),
        ADSL(3),
        MIX(4),
        VLAN_MIX(5),
    }
}