package ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.DynamicWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix.MixDenamicWan
 *
 * @author Score2
 * @since 2022/06/08 20:38
 */
class MixDynamicWan(
    wanId: Int,
    router: IkuaiRouter,
    override var json: JsonObject
) : MixIndividualWan, DynamicWan {

}