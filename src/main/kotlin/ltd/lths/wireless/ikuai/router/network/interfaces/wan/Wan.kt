package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.entourage.api.asNumBool
import ltd.lths.wireless.ikuai.entourage.api.asNumSign
import ltd.lths.wireless.ikuai.entourage.util.ActionProp

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.`interface`.wan.Wan
 *
 * @author Score2
 * @since 2022/06/08 17:47
 */
abstract class Wan(val wanId: Int, val router: IkuaiRouter) {

    open val json get() = ActionProp.actionWanShow(router, wanId).postJson(router)

    val interfaceName: String
        get() = "wan$wanId"

    var isDefaultRouter: Boolean
        get() = json.get("default_route").asNumBool
        set(value) = json.addProperty("default_route", value.asNumSign)

    val ethernets get() = router.lanWanSettings.ethernets.filter { it.`interface` == "wan$wanId" }

    enum class Type(val internet: Int) {
        STATIC(0),
        DYNAMIC(1),
        ADSL(2),
        MIX(3),
        VLAN_MIX(4),
        ;
        companion object {
            fun find(internet: Int) =
                values().find { it.internet == internet }!!
        }
    }
}