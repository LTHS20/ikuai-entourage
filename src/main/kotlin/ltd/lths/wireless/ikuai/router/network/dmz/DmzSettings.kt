package ltd.lths.wireless.ikuai.router.network.dmz

import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.util.ActionProp
import ltd.lths.wireless.ikuai.entourage.util.data
import ltd.lths.wireless.ikuai.entourage.util.dataArray
import ltd.lths.wireless.ikuai.router.IkuaiRouter

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.dmz.DmzSettings
 *
 * @author Score2
 * @since 2022/06/13 22:03
 */
class DmzSettings(val router: IkuaiRouter) {

    val json get() = ActionProp.actionDmzShow(router)

/*    var dmzs
        get() = json.toJson().dataArray.map {
            Dmz(router, it.asJsonObject)
        }.toMutableList()
        set(value) {
            field.losslessUpdate(
                value,

            )

            return TODO()
        }*/


}