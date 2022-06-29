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

    var dmzs = listOf<Dmz>()
        get() {
            field = json.toJson().dataArray.map {
                Dmz(router, it.asJsonObject)
            }.toList()
            return field
        }
        set(value) {
            field.toMutableList().losslessUpdate(
                value,
                accord = { t, t1 ->
                    t.id == t1.id
                },
                adding = {
                    ActionProp.actionDmzAdd(router, it)
                    true
                },
                removing = {
                    ActionProp.actionDmzDel(router, it)
                    true
                },
                keepers = { t, t1 ->
                    ActionProp.actionDmzEdit(router, t1)
                    true
                }
            )

            return
        }


}