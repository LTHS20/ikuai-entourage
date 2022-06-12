package ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.StaticWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixStaticWan
 *
 * @author Score2
 * @since 2022/06/08 20:39
 */
class MixStaticWan(
    wanId: Int,
    ac: IkuaiRouter,
    override var json: JsonObject
) : MixIndividualWan, StaticWan {

}