package ltd.lths.wireless.ikuai.entourage.api

import ltd.lths.wireless.ikuai.entourage.Entourage

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
 * @return 更新后的 list
 */
fun <T> MutableList<T>.losslessUpdate(
    newList: List<T>,
    accord: (T, T) -> Boolean = { t1, t2 -> t1 == t2 },
    adding: (T) -> Boolean = { true },
    removing: (T) -> Boolean = { true }
): MutableList<T> {
    val addList = newList.filter { t1 -> !this.any { t2 -> accord(t1, t2) } }.mapNotNull { t ->
        println("ad $t")
        return@mapNotNull if (adding(t)) t else null
    }

    val removeList = this.filter { t1 -> !newList.any { t2 -> accord(t1, t2) } }.mapNotNull { t ->
        println("rm $t")
        return@mapNotNull if (removing(t)) t else null
    }

    addList.forEach { this.add(it) }
    removeList.forEach { this.remove(it) }

    return this
}