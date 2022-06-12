package ltd.lths.wireless.ikuai.entourage.util

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixIndividualWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.util.ActionProp
 *
 * @author Score2
 * @since 2022/06/10 21:08
 */
class ActionProp(val funcName: String, val action: String, val param: JsonObject) {

    constructor(funcName: String, action: String, vararg param: Pair<String, Any>): this(
        funcName,
        action,
        JsonObject().run {
            param.forEach { (k, v) ->
                when (v) {
                    is Boolean -> addProperty(k, v)
                    is Number -> addProperty(k, v)
                    else -> addProperty(k, v.toString())
                }
            }

            this
        }
    )

    fun post(ac: IkuaiRouter) =
        ac.post(this)

    fun postJson(ac: IkuaiRouter, action: IkuaiRouter.PostAction = IkuaiRouter.PostAction.CALL) =
        ac.postJson(action, this)

    fun toJson(): JsonObject {
        return JsonObject().run {
            addProperty("func_name", funcName)
            addProperty("action", action)
            add("param", param)
            this
        }
    }

    override fun toString(): String {
        return toJson().toString()
    }

    companion object {

        fun actionWanShow(ac: IkuaiRouter, wanId: Int) = ac.prop(
            "wan",
            "show",

            "id" to wanId,
            "TYPE" to "support_wisp,total,data"
        )

        fun actionLanShow(ac: IkuaiRouter, lanId: Int) = ac.prop(
            "lan",
            "show",

            "id" to lanId,
            "TYPE" to "support_wisp,total,data"
        )

        fun actionShowEtherInfo(ac: IkuaiRouter) = ac.prop(
            "homepage",
            "show",

            "TYPE" to "ether_info,snapshoot"
        )

        fun actionMixWanShow(ac: IkuaiRouter, wanId: Int, vlanInternet: Int) = ac.prop(
            "wan",
            "show",

            "ORDER" to "desc",
            "ORDER_BY" to "id",
            "TYPE" to "vlan_data,vlan_total",
            "interface" to "wan$wanId",
            "limit" to "0,20",
            "vlan_internet" to vlanInternet
        )

        fun actionMixWanAdd(ac: IkuaiRouter, wan: MixIndividualWan) = ac.prop(
            "wan",
            "vlan_add",
            wan.json
        )

        fun actionMixWanDel(ac: IkuaiRouter, wan: MixIndividualWan) = ac.prop(
            "wan",
            "vlan_del",
            "id" to wan.id
        )

    }
}

val JsonObject.vlanData get() = getAsJsonObject("Data").getAsJsonArray("vlan_data")
val JsonObject.data get() = getAsJsonObject("Data").getAsJsonArray("data")[0].asJsonObject
