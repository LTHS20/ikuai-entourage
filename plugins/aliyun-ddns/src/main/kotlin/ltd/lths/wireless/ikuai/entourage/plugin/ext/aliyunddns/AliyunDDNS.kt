package ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns

import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.element.Domain
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.MixWan
import ltd.lths.wireless.ikuai.entourage.Entourage
import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.addAliyunRecord
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.delAliyunRecord
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.getAliyunRecords
import taboolib.common.platform.function.submit

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.AliyunDDNS
 *
 * @author Score2
 * @since 2022/06/11 14:58
 */
//@RuntimeDependencies(
//    RuntimeDependency("!com.aliyun:aliyun-java-sdk-core:4.6.0", test = "!com.aliyuncs.AcsError"),
//    RuntimeDependency("!com.aliyun:aliyun-java-sdk-alidns:2.6.32", test = "!com.aliyuncs.alidns.model.v20150109.AddCustomLineRequest")
//)
object AliyunDDNS : EntouragePlugin("aliyun-ddns") {

    var running = false
        private set

    val accessKeyId get() = config.getString("aliyun.access-key-id", "xxx")!!
    val secret get() = config.getString("aliyun.secret", "xxx")!!
    val domainName get() = config.getString("aliyun.domain-name", "xxx")!!
    val regionId get() = config.getString("aliyun.region-id", "cn-hangzhou")!!

    val refreshInterval get() = config.getLong("refresh-interval", 60)
    val refreshACName get() = config.getString("refresh-ac.name", "test")
    val refreshACWanId get() = config.getInt("refresh-ac.wan-id", 1)

    override fun onEnable() {
        if (arrayOf(accessKeyId, secret, domainName).contains("xxx")) {
            logger.info("§c请根据配置文件内容完成填写后输入 /pl $name reload 来启用该插件的功能.")
            return
        }
        start()
    }

    override fun onReload() {
        start()
    }

    fun start() {
        if (running) {
            return
        }
        running = true
        logger.info("开始刷新, 间隔 $refreshInterval 秒, 将针对 ac $refreshACName 进行刷新")
        submit(async = true, period = refreshInterval * 20) {
            val router = Entourage.bindRouters.find { refreshACName == it.name } ?: return@submit logger.info("§c获取对应绑定 AC $refreshACName 失败.")
            val iKuaiDomains = router.lanWanSettings.getWan(1, MixWan::class.java).AdslWans.map { Domain("@", "A", it.ip) }
            val aliyunDomains = getAliyunRecords().filter { it.rR == "@" }

            aliyunDomains.toMutableList().losslessUpdate(
                iKuaiDomains,
                accord = { t, t1 ->
                    t.value == t1.value
                },
                adding = { t ->
                    addAliyunRecord(t)
                    logger.info("已更新 ${t.value} 于 Aliyun")
                    false
                },
                removing = { t ->
                    delAliyunRecord(t)
                    logger.info("已删除 ${t.value} 于 Aliyun")
                    false
                },
                keepers = { _, _ ->
                    false
                }
            )

        }
    }
}