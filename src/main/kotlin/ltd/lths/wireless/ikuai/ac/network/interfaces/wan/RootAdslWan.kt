package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.api.asNumBool
import ltd.lths.wireless.ikuai.entourage.api.asNumSign
import ltd.lths.wireless.ikuai.entourage.api.asWrittenBool
import ltd.lths.wireless.ikuai.entourage.api.asWrittenString

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
open class RootAdslWan(
    wanId: Int,
    ac: IkuaiAC
): Wan(wanId, ac), AdslWan {


}