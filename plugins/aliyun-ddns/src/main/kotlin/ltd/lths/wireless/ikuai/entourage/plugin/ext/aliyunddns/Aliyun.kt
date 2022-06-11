package ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordRequest
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordRequest
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.profile.DefaultProfile
import com.google.gson.Gson
import com.google.gson.JsonParser
import ltd.lths.wireless.ikuai.entourage.api.println
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.AliyunDDNS.accessKeyId
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.AliyunDDNS.domainName
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.AliyunDDNS.regionId
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.AliyunDDNS.secret
import ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.element.Domain

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.plugin.ext.aliyunddns.Aliyun
 *
 * @author Score2
 * @since 2022/06/11 23:52
 */
object Aliyun {

    val profile by lazy { DefaultProfile.getProfile(regionId, accessKeyId, secret) }

    val client by lazy { DefaultAcsClient(profile) }


    /**
     * @return 所有解析的域名
     */
    fun getAliyunRecords(): List<Domain> {
        val request = DescribeDomainRecordsRequest()
        request.domainName = domainName

        val json = try {
            val response: DescribeDomainRecordsResponse = client.getAcsResponse(request)
            JsonParser.parseString(Gson().toJson(response))
        } catch (e: ServerException) {
            e.printStackTrace()
            return emptyList()
        } catch (e: ClientException) {
            println("ErrCode:" + e.errCode)
            println("ErrMsg:" + e.errMsg)
            println("RequestId:" + e.requestId)
            return emptyList()
        }
        val jsonObject = json.asJsonObject

        val list = mutableListOf<Domain>()
        jsonObject.getAsJsonArray("domainRecords").forEach {
            it.asJsonObject.run {
                list.add(
                    Domain(
                        get("recordId").asString,
                        get("rR").asString,
                        get("type").asString,
                        get("value").asString,
                        get("tTL").asLong,
                    )
                )
            }
        }
        return list
    }

    /**
     * @param domain 域名
     * @return recordId
     */
    fun addAliyunRecord(domain: Domain): String? {
        val request = AddDomainRecordRequest()
        request.domainName = domainName
        request.rr = domain.rR
        request.type = domain.type
        request.value = domain.value
        request.ttl = domain.tTL
        val json = try {
            val response = client.getAcsResponse(request)
            JsonParser.parseString(Gson().toJson(response))
        } catch (e: ServerException) {
            e.printStackTrace()
            return null
        } catch (e: ClientException) {
            println("ErrCode:" + e.errCode)
            println("ErrMsg:" + e.errMsg)
            println("RequestId:" + e.requestId)
            return null
        }
        val jsonObject = json.asJsonObject

        return jsonObject.get("recordId").asString
    }

    fun delAliyunRecord(domain: Domain): Boolean =
        if (domain.recordId != null) delAliyunRecord(domain.recordId)
        else false


    /**
     * @param recordId 记录id
     * @return 是否成功
     */
    fun delAliyunRecord(recordId: String): Boolean {
        val request = DeleteDomainRecordRequest()
        request.recordId = recordId
        try {
            client.getAcsResponse(request)
        } catch (e: ServerException) {
            e.printStackTrace()
            return false
        } catch (e: ClientException) {
            println("ErrCode:" + e.errCode)
            println("ErrMsg:" + e.errMsg)
            println("RequestId:" + e.requestId)
            return false
        }

        return true
    }
}