package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import ltd.lths.wireless.ikuai.router.IkuaiRouter

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
open class RootAdslWan(
    wanId: Int,
    ac: IkuaiRouter
): Wan(wanId, ac), AdslWan {


}