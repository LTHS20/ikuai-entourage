package ltd.lths.wireless.ikuai.ac.network.interfaces.wan

import com.google.gson.JsonObject
import ltd.lths.wireless.ikuai.ac.IkuaiAC
import ltd.lths.wireless.ikuai.entourage.util.Persistent

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.network.interfaces.wan.AdslWan
 *
 * @author Score2
 * @since 2022/06/08 18:17
 */
open class AdslWan(
    val ac: IkuaiAC,
    val account: String,
    val password: String
) : Persistent {

    override var element: JsonObject
        get() = TODO("Not yet implemented")
        set(value) {}

//    var isEnabled: Boolean
//
//    var isDefaultGateway: Boolean

}