package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import ltd.lths.wireless.ikuai.ac.IkuaiAC

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.`interface`.wan.StaticIPWan
 *
 * @author Score2
 * @since 2022/06/08 17:50
 */
open class RootStaticWan(
    wanId: Int,
    ac: IkuaiAC
) : Wan(wanId, ac), StaticWan {
}