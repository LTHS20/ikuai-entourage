package ltd.lths.wireless.ikuai.router.network.interfaces.lan

import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.entourage.util.ActionProp

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.lan.Lan
 *
 * @author Score2
 * @since 2022/06/09 23:39
 */
class Lan(val lanId: Int, val router: IkuaiRouter) {

    val json get() = ActionProp.actionLanShow(router, lanId).postJson(router)

    val ethernets get() = router.lanWanSettings.ethernets.filter { it.`interface` == "lan$lanId" }
}