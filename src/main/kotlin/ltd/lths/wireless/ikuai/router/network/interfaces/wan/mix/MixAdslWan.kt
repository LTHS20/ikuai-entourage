package ltd.lths.wireless.ikuai.router.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.router.IkuaiRouter
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.AdslWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixAdslWan
 *
 * @author Score2
 * @since 2022/06/08 20:39
 */
class MixAdslWan(
    wanId: Int,
    ac: IkuaiRouter,
    override var json: JsonObject
) : MixIndividualWan, AdslWan {

}