package ltd.lths.wireless.ikuai.entourage.api

import com.google.gson.JsonElement
import ltd.lths.wireless.ikuai.entourage.Entourage
import ltd.lths.wireless.ikuai.entourage.Entourage.logger
import java.security.MessageDigest
import java.util.*

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