package ltd.lths.wireless.ikuai.router

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import ltd.lths.wireless.ikuai.router.network.interfaces.LanWanSettings
import ltd.lths.wireless.ikuai.entourage.Entourage.config
import ltd.lths.wireless.ikuai.entourage.api.base64
import ltd.lths.wireless.ikuai.entourage.api.md5
import ltd.lths.wireless.ikuai.entourage.util.ActionProp
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import taboolib.library.configuration.ConfigurationSection
import java.net.InetAddress

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.ac.IkuaiAC
 *
 * @author Score2
 * @since 2022/06/08 17:27
 */
class IkuaiRouter(
    val name: String,
    var ip: String,
    var username: String,
    var password: String,
    port: Int? = null,
    useSSL: Boolean = false,
) {

    constructor(
        name: String,
        host: InetAddress,
        username: String,
        password: String,
        port: Int? = null,
        useSSL: Boolean = false
    ) : this(
        name, host.hostAddress, username, password, port, useSSL
    )

    constructor(
        name: String,
        section: ConfigurationSection
    ) : this(
        name,
        section.getString("host")!!.substringBeforeLast(":"),
        section.getString("username", "admin")!!,
        section.getString("password", "admin")!!,
        if (section.getString("host")!!.contains(":"))
            section.getString("host")!!.substringAfterLast(":").toInt()
        else if (section.contains("port"))
            section.getInt("port", 80)
        else 80,
        section.getBoolean("use-ssl")
    )

    var httpType = if (useSSL) "https" else "http"
    var port = port ?: if (useSSL) 443 else 80

    fun index(index: String): String {
        return "$httpType://$ip:$port/$index"
    }

    val client get() = HttpClients.custom()
        .setDefaultRequestConfig(
            RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                .setConnectionRequestTimeout(config.getInt("connection-request-timeout"))
                .setConnectTimeout(config.getInt("connect-timeout"))
                .setSocketTimeout(config.getInt("socket-timeout"))
                .build()
        )
        .build()

    var cookie: String = ""
        private set
        get() {
            val logged = let {
                if (field.isBlank()) {
                    return@let false
                }
                // 不能省略这一步, 否则会递归
                val post = HttpPost(index("Action/call"))
                post.setHeader("Cookie", "username=$username; login=1; sess_key=$field")
                post.setHeader("Content-Type", "application/json;charset=UTF-8")

                val response = client.execute(post)

                val json = JsonParser.parseString(EntityUtils.toString(response.entity))
                val jsonObject = json.asJsonObject

                val result = jsonObject.get("Result").asInt
                if (result == 10014) {
                    return@let false
                }

                return@let true
            }
            if (!logged) {
                val response = post(PostAction.LOGIN, JsonObject().also {
                    it.addProperty("pass", "salt_11$password".base64)
                    it.addProperty("passwd", password.md5)
                    it.addProperty("remember_password", "true")
                    it.addProperty("username", username)
                }.toString())

                if (JsonParser.parseString(EntityUtils.toString(response.entity)).asJsonObject.get("Result").asInt == 10001) {
                    return "password"
                }

                return response.getLastHeader("Set-Cookie").value.substringBefore(";").removePrefix("sess_key=").also { field = it }
            }
            return field
        }


    val lanWanSettings = LanWanSettings(this)


    fun post(entity: ActionProp) =
        post(PostAction.CALL, StringEntity(entity.toString(), Charsets.UTF_8))

    fun post(entity: JsonObject) =
        post(PostAction.CALL, StringEntity(entity.toString(), Charsets.UTF_8))

    fun post(entity: String) =
        post(PostAction.CALL, StringEntity(entity, Charsets.UTF_8))

    fun post(entity: HttpEntity? = null) =
        post(PostAction.CALL, entity)

    fun post(action: PostAction, entity: ActionProp) =
        post(action, StringEntity(entity.toString(), Charsets.UTF_8))

    fun post(action: PostAction, entity: JsonObject) =
        post(action, StringEntity(entity.toString(), Charsets.UTF_8))

    fun post(action: PostAction, entity: String) =
        post(action, StringEntity(entity, Charsets.UTF_8))

    fun post(action: PostAction, entity: HttpEntity? = null): HttpResponse {
        val post = HttpPost(index("Action/${action.name.lowercase()}"))
        if (action != PostAction.LOGIN) {
            post.setHeader("Cookie", "username=$username; login=1; sess_key=$cookie")
            post.setHeader("Content-Type", "application/json;charset=UTF-8")
        }
        post.entity = entity
        return client.execute(post)
    }


    fun postString(entity: ActionProp) =
        postString(entity.toString())

    fun postString(entity: JsonObject) =
        postString(entity.toString())

    fun postString(entity: String) =
        postString(PostAction.CALL, entity)

    fun postString(entity: HttpEntity? = null) =
        postString(PostAction.CALL, entity)

    fun postString(action: PostAction, entity: ActionProp) =
        postString(action, entity.toString())

    fun postString(action: PostAction, entity: JsonObject) =
        postString(action, entity.toString())

    fun postString(action: PostAction, entity: String) =
        postString(action, StringEntity(entity, Charsets.UTF_8))

    fun postString(action: PostAction, entity: HttpEntity? = null): String =
        EntityUtils.toString(post(action, entity).entity)


    fun postJson(entity: ActionProp) =
        postJson(entity.toJson())

    fun postJson(entity: JsonObject) =
        postJson(entity.toString())

    fun postJson(entity: String) =
        postJson(PostAction.CALL, entity)

    fun postJson(entity: HttpEntity? = null) =
        postJson(PostAction.CALL, entity)

    fun postJson(action: PostAction, entity: ActionProp) =
        postJson(action, entity.toString())

    fun postJson(action: PostAction, entity: JsonObject) =
        postJson(action, entity.toString())

    fun postJson(action: PostAction, entity: String) =
        postJson(action, StringEntity(entity, Charsets.UTF_8))

    fun postJson(action: PostAction, entity: HttpEntity? = null): JsonObject =
        JsonParser.parseString(postString(action, entity)).asJsonObject


    fun prop(funcName: String, action: String, vararg param: Pair<String, Any>) =
        ActionProp(funcName, action, *param)

    fun prop(funcName: String, action: String, param: JsonObject) =
        ActionProp(funcName, action, param)

    enum class PostAction {
        LOGIN,
        CALL
    }
}