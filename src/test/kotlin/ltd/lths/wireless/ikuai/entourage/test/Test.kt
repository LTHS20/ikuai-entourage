package ltd.lths.wireless.ikuai.entourage.test

import ltd.lths.wireless.ikuai.entourage.api.asIpv4
import ltd.lths.wireless.ikuai.entourage.api.asIpv4Decimal


/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.test.Test
 *
 * @author Score2
 * @since 2022/06/08 21:44
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
/*        val origin = mutableListOf("abc", "ghi", "jkl")
        val target = mutableListOf("abc", "def", "jkl", "jkl")

        val start = System.currentTimeMillis()
        println(origin.losslessUpdate(target))
        println(target)
        println("${System.currentTimeMillis() - start}ms")*/

        println("172.18.0.20".asIpv4Decimal)
        println("172.18.0.21".asIpv4Decimal)
        println("172.18.0.20".asIpv4Decimal.asIpv4)
        println("172.18.0.21".asIpv4Decimal.asIpv4)

//        println("2886860820".toInt(256))

    }


}