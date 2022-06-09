package ltd.lths.wireless.ikuai.entourage.util

import taboolib.common.reflect.Reflex.Companion.invokeMethod
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * @project TrMenu
 *
 * @author Score2
 * @since 2021/09/30 12:11
 */
internal object UNINITIALIZED_VALUE
internal object RELOAD_VALUE

class Reloadable<T>(val initializer: () -> T){

    private var _value: Any? = UNINITIALIZED_VALUE

    var thisRef: Any? = null
    var property: KProperty<*>? = null

    init {
        // TODO 备选方案: 根据栈取得类名
        Exception().stackTrace.forEach {
            println(it)
        }

    }

    fun reload(): T {
        val result = initializer()
        _value = result
        return result
    }

    fun isInitialized() =
        _value != UNINITIALIZED_VALUE


    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        this.thisRef = thisRef
        this.property = property
        reloadables.add(this)
        println("$thisRef, thank you for delegating '${property.name}' to me!")

        if (_value != UNINITIALIZED_VALUE) {
            @Suppress("UNCHECKED_CAST")
            return _value as T
        }
        return reload()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
        this.thisRef = thisRef
        this.property = property
        reloadables.add(this)

        if (value == RELOAD_VALUE) {
            reload()
            return
        }
        _value = value
    }

}

class NotPropertyReloadable(name: String) : Exception("This property '$name' can not to reload, because it doesn't have access to 'Reloadable'!")

val reloadables = mutableSetOf<Reloadable<*>>()

/*operator fun <T> Reloadable<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    println("111")
    reloadableProperties.putIfAbsent(property, this)
    return value
}

operator fun <T> T.setValue() {

}*/

fun <T> KProperty<T>.reload() {
    kotlin.runCatching {

        Exception().stackTrace.forEach {
            println(it)
        }

        reloadables.find { it.property == this }!!.reload()

    }.onFailure {
        throw NotPropertyReloadable(this.name)
    }
}


fun <T> reloadable(initializer: () -> T) = Reloadable(initializer)