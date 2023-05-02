package ltd.lths.wireless.ikuai.router.network.interfaces.wan

import ltd.lths.wireless.ikuai.router.IkuaiRouter

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.`interface`.wan.StaticIPWan
 *
 * @author Score2
 * @since 2022/06/08 17:50
 */
open class RootStaticWan(
    wanId: Int,
    router: IkuaiRouter
) : Wan(wanId, router), StaticWan {
}