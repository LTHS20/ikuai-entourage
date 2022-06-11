package ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.element

/**
 * iKuai-DDNS
 * ltd.lths.ikuai.ddns.aliyun.element.Domain
 *
 * @param recordId 记录id
 * @param rR 主机记录
 * @param type 记录类型
 * @param value 记录值
 * @param tTL TTL
 *
 * @author Score2
 * @since 2022/03/02 22:11
 */
data class Domain(
    val recordId: String? = null,
    val rR: String,
    val type: String,
    val value: String,
    val tTL: Long = 600,
) {
    constructor(rR: String, type: String, value: String, tTL: Long = 600): this(null, rR, type, value, tTL)

    override fun toString(): String {
        return "[recordId=$recordId, rR=$rR, type=$type, value=$value, tTL=$tTL]"
    }
}