package ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixAdslWan
 *
 * @author Score2
 * @since 2022/06/08 20:39
 */
class MixAdslWan(
    ac: IkuaiAC,
    val vlanId: Int,
    val name: String,
    account: String,
    password: String
) : AdslWan(ac, account, password) {
}