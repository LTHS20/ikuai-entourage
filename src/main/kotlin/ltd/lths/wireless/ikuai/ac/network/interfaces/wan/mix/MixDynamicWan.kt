package ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.ac.network.interfaces.wan.DynamicWan

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.mix.MixDenamicWan
 *
 * @author Score2
 * @since 2022/06/08 20:38
 */
class MixDynamicWan(
    wanId: Int,
    ac: IkuaiAC,
    override var json: JsonObject
) : MixIndividualWan, DynamicWan {

}