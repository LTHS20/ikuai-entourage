package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixAdslWan
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixDynamicWan
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixStaticWan
import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.util.ActionProp
import ltd.lths.wireless.ikuai.entourage.util.vlanData

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.MixWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
class MixWan(wanId: Int, ac: IkuaiRouter) : Wan(wanId, ac) {

    var assMulti: Int
        get() = json.get("ass_multi_total").asInt
        set(value) = json.addProperty("ass_multi_total", value)

    var staticWans = mutableListOf<MixStaticWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(router, wanId, Type.STATIC.internet).postJson(router).vlanData.map {
                    MixStaticWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.STATIC.internet)).vlanData.map {
                    MixStaticWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
        }

    var dynamicWans = mutableListOf<MixDynamicWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(router, wanId, Type.DYNAMIC.internet).postJson(router).vlanData.map {
                    MixDynamicWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.DYNAMIC.internet)).vlanData.map {
                    MixDynamicWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
        }

    var AdslWans = mutableListOf<MixAdslWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(router, wanId, Type.ADSL.internet).postJson(router).vlanData.map {
                    MixAdslWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.ADSL.internet)).vlanData.map {
                    MixAdslWan(wanId, router, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                    true
                }
            )
        }

    enum class Type(val internet: Int) {
        STATIC(0),
        DYNAMIC(1),
        ADSL(2),
    }
}