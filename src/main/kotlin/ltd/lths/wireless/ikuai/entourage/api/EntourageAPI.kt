package ltd.lths.wireless.ikuai.entourage.api

import com.google.gson.JsonElement
import ltd.lths.wireless.ikuai.entourage.Entourage.logger
import java.security.MessageDigest
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.api.EntourageAPI
 *
 * @author Score2
 * @since 2022/06/08 1:11
 */
/**
 * 根据指定字段值比对实现无损更新 List 的元素
 *
 * @param newList 新的 list
 * @param accord 根据指定字段值判断是否符合(原, 目标)
 * @param adding 增加元素时的操作, 手动决定是否增加
 * @param removing 删减元素时的操作, 手动决定是否删除
 * @param keepers 对那些没有被添加或删除的元素进行操作
 * @return 更新后的 list
 */
fun <T> MutableList<T>.losslessUpdate(
    newList: List<T>,
    accord: (T, T) -> Boolean = { t, t1 -> t == t1 },
    adding: (T) -> Boolean = { true },
    removing: (T) -> Boolean = { true },
    keepers: ((T, T) -> Boolean)? = null
): MutableList<T> {
    val addList = newList.filter { t1 -> !this.any { t2 -> accord(t1, t2) } }.mapNotNull { t ->
        return@mapNotNull if (adding(t)) t else null
    }

    val removeList = this.filter { t1 -> !newList.any { t2 -> accord(t1, t2) } }.mapNotNull { t ->
        return@mapNotNull if (removing(t)) t else null
    }

    addList.forEach { this.add(it) }
    removeList.forEach { this.remove(it) }

    keepers ?: return this

    val oriKeepers = filter { !addList.contains(it) && !removeList.contains(it) }
    val newKeepers = newList.filter { !addList.contains(it) && !removeList.contains(it) }
    for (i in oriKeepers.indices) {
        if (!keepers(oriKeepers[i], newKeepers[i])) {
            remove(oriKeepers[i])
        }
    }

    return this
}

/**
 * IPv4地址转换为int类型数字
 *
 */
fun String.toIpv4Int(): Int {
    // 判断是否是ip格式的
    if (!isIPv4Address(this)) throw RuntimeException("Invalid ip address")

    // 匹配数字
    val pattern: Pattern = Pattern.compile("\\d+")
    val matcher: Matcher = pattern.matcher(this)
    var result = 0
    var counter = 0
    while (matcher.find()) {
        val value: Int = matcher.group().toInt()
        result = value shl 8 * (3 - counter++) or result
    }
    return result
}

/**
 * 判断是否为ipv4地址
 *
 */
private fun isIPv4Address(ipv4Addr: String): Boolean {
    val lower = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" // 0-255的数字
    val regex = "$lower(\\.$lower){3}"
    val pattern: Pattern = Pattern.compile(regex)
    val matcher: Matcher = pattern.matcher(ipv4Addr)
    return matcher.matches()
}

/**
 * 将int数字转换成ipv4地址
 *
 */
fun Int.toIpv4(): String {
    val sb = StringBuilder()
    var num = 0
    var needPoint = false // 是否需要加入'.'
    for (i in 0..3) {
        if (needPoint) {
            sb.append('.')
        }
        needPoint = true
        val offset = 8 * (3 - i)
        num = this shr offset and 0xff
        sb.append(num)
    }
    return sb.toString()
}


val String.md5: String get() {
    val messageDigest = MessageDigest.getInstance("MD5")
    val inputByteArray: ByteArray = toByteArray(Charsets.UTF_8)
    messageDigest.update(inputByteArray)

    val byteArray = messageDigest.digest()
    val resultCharArray = CharArray(byteArray.size * 2)

    val hexDigits = "0123456789abcdef"
    var index = 0
    byteArray.forEach { b ->
        val i = b.toInt()
        resultCharArray[index++] = hexDigits[i ushr 4 and 0xf]
        resultCharArray[index++] = hexDigits[i and 0xf]
    }

    return String(resultCharArray)
}

val String.base64: String get() =
    Base64.getEncoder().encodeToString(this.toByteArray())

val JsonElement.asWrittenBool get() = asString == "yes"
val JsonElement.asNumBool get() = asInt == 1

val Boolean.asWrittenString get() = if (this) "yes" else "no"
val Boolean.asNumSign get() = if (this) 1 else 2

fun <T> T.println() = logger.info(this)