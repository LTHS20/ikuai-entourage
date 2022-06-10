package ltd.lths.wireless.ikuai.ac.network.interfaces.lan

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.util.ActionProp

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.lan.Lan
 *
 * @author Score2
 * @since 2022/06/09 23:39
 */
class Lan(val lanId: Int, val ac: IkuaiAC) {

    val json get() = ActionProp.actionLanShow(ac, lanId).postJson(ac)

    val ethernets get() = ac.lanWanSettings.ethernets.filter { it.`interface` == "lan$lanId" }
}