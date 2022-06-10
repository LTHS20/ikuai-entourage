package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import ltd.lths.wireless.ikuai.ac.IkuaiAC

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.`interface`.wan.DhcpWan
 *
 * @author Score2
 * @since 2022/06/08 17:50
 */
open class RootDynamicWan(
    wanId: Int,
    ac: IkuaiAC
) : Wan(wanId, ac), DynamicWan {
}