package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixAdslWan
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixDynamicWan
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixStaticWan
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
class MixWan(wanId: Int, ac: IkuaiAC) : Wan(wanId, ac) {

    var assMulti: Int
        get() = json.get("ass_multi_total").asInt
        set(value) = json.addProperty("ass_multi_total", value)

    var staticWans = mutableListOf<MixStaticWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(ac, wanId, Type.STATIC.internet).postJson(ac).vlanData.map {
                    MixStaticWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                ac.postJson(ActionProp.actionMixWanShow(ac, wanId, Type.STATIC.internet)).vlanData.map {
                    MixStaticWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
        }

    var dynamicWans = mutableListOf<MixDynamicWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(ac, wanId, Type.DYNAMIC.internet).postJson(ac).vlanData.map {
                    MixDynamicWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                ac.postJson(ActionProp.actionMixWanShow(ac, wanId, Type.DYNAMIC.internet)).vlanData.map {
                    MixDynamicWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
        }

    var AdslWans = mutableListOf<MixAdslWan>()
        get() {
            field = field.losslessUpdate(
                ActionProp.actionMixWanShow(ac, wanId, Type.ADSL.internet).postJson(ac).vlanData.map {
                    MixAdslWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
            return field
        }
        set(value) {
            value.losslessUpdate(
                ac.postJson(ActionProp.actionMixWanShow(ac, wanId, Type.ADSL.internet)).vlanData.map {
                    MixAdslWan(wanId, ac, it.asJsonObject)
                },
                accord =  { t, t1 ->
                    t.vlanName == t1.vlanName
                },
                keepers = { t, t1 ->
                    t.json = t1.json
                }
            )
        }

    enum class Type(val internet: Int) {
        STATIC(0),
        DYNAMIC(1),
        ADSL(2),
    }
}