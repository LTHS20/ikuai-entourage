package ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns

import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.element.Domain
import ltd.lths.wireless.ikuai.router.network.interfaces.wan.MixWan
import ltd.lths.wireless.ikuai.entourage.Entourage
import ltd.lths.wireless.ikuai.entourage.api.isIpv4
import ltd.lths.wireless.ikuai.entourage.api.losslessUpdate
import ltd.lths.wireless.ikuai.entourage.plugin.EntouragePlugin
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.addAliyunRecord
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.delAliyunRecord
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun.getAliyunRecords
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

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

    val accessKeyId get() = config.getString("aliyun.access-key-id", "xxx")!!
    val secret get() = config.getString("aliyun.secret", "xxx")!!
    val domainName get() = config.getString("aliyun.domain-name", "xxx")!!
    val regionId get() = config.getString("aliyun.region-id", "cn-hangzhou")!!
    val domainHeader get() = config.getString("domain-header", "@")!!

    val ignoreCount get() = config.getInt("ignore-count", 2)

    val refreshInterval get() = config.getLong("refresh-interval", 60)
    val refreshEntourageName get() = config.getString("refresh-entourage.name", "test")
    val refreshEntourageWanId get() = config.getInt("refresh-entourage.wan-id", 1)


    override fun onEnable() {
        if (arrayOf(accessKeyId, secret, domainName).contains("xxx")) {
            logger.info("§c请根据配置文件内容完成填写后输入 /pl $name reload 来启用该插件的功能.")
            return
        }
        logger.info("开始刷新, 间隔 $refreshInterval 秒, 将针对 router $refreshEntourageName 进行刷新")
        Thread {
            while (true) {
                kotlin.runCatching {
                    update()
                }.onFailure {
                    it.printStackTrace()
                }
                Thread.sleep(refreshInterval * 1000)
            }
        }.start()
    }

    override fun onReload() {
        logger.info("重新载入成功, 将会手动触发一次更新")
        update()
    }

    fun update() {
        val router = kotlin.runCatching { Entourage.bindRouters.find { refreshEntourageName == it.name } }.onFailure { it.printStackTrace() }.getOrNull() ?: return logger.info("§c获取对应绑定 router $refreshEntourageName 失败.")
        val iKuaiDomains = router.lanWanSettings.getWan(refreshEntourageWanId, MixWan::class.java).adslWans.mapNotNull {
            if (it.ip.isIpv4) Domain(domainHeader, "A", it.ip)
            else null
        }
        if (iKuaiDomains.size - ignoreCount < 1) {
            logger.info("ip 数量少于 ${ignoreCount + 1}, 更新失败")
            return
        }
        val aliyunDomains = getAliyunRecords().filter { it.rR == domainHeader }

        kotlin.runCatching {
            aliyunDomains.toMutableList().losslessUpdate(
                iKuaiDomains,
                accord = { t, t1 ->
                    t.value == t1.value
                },
                adding = { t ->
                    addAliyunRecord(t)
                    logger.info("已更新 ${t.value} 于 Aliyun")
                    true
                },
                removing = { t ->
                    delAliyunRecord(t)
                    logger.info("已删除 ${t.value} 于 Aliyun")
                    true
                },
                keepers = { _, _ ->
                    true
                }
            )
        }.onFailure {
            logger.info("DDNS 刷新时遇到问题")
            logger.info("ikuai list: ${iKuaiDomains.joinToString { it.value }}")
            logger.info("aliyun list: ${aliyunDomains.joinToString { it.value }}")
        }
    }
}