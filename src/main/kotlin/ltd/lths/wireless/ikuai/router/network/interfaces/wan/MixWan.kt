package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixAdslWan
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixDynamicWan
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixStaticWan
import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.util.ActionProp
import ltd.lths.wireless.ikuai.entourage.util.vlanDataArray
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixIndividualWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.interfaces.wan.MixWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
class MixWan(wanId: Int, router: IkuaiRouter) : Wan(wanId, router) {

    var assMulti: Int
        get() = json.get("ass_multi_total").asInt
        set(value) = json.addProperty("ass_multi_total", value)

    var staticWans = mutableListOf<MixStaticWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(router, wanId, Type.STATIC.internet).postJson(router).vlanDataArray.map {
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
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.STATIC.internet)).vlanDataArray.map {
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
                ActionProp.actionMixWanShow(router, wanId, Type.DYNAMIC.internet).postJson(router).vlanDataArray.map {
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
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.DYNAMIC.internet)).vlanDataArray.map {
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

    var adslWans = mutableListOf<MixAdslWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(router, wanId, Type.ADSL.internet).postJson(router).vlanDataArray.map {
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
                router.postJson(ActionProp.actionMixWanShow(router, wanId, Type.ADSL.internet)).vlanDataArray.map {
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

    val individualWans get() = mutableListOf<MixIndividualWan>().run {
        addAll(staticWans)
        addAll(dynamicWans)
        addAll(adslWans)
        this
    }

    enum class Type(val internet: Int) {
        STATIC(0),
        DYNAMIC(1),
        ADSL(2),
    }
}