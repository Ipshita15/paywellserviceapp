package com.cloudwell.paywell.services.retrofit

import android.content.Context
import android.util.Base64
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.DateUtils
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class AppsAuthHeaderTokenInterceptor(val mContext: AppController?) : Interceptor {

    var authHeader:String = ""

    fun HeaderTokenInterceptor(context: Context) {

    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var requestBody = chain.request().body()
        val toString = chain.request().url().toString()

        if (toString.equals("https://agentapi.paywellonline.com/PaywellAuth/getToken?") || toString.equals("https://agentapi.paywellonline.com/PaywelltransactionRetailer/userServiceProfiling_v2")){
            val newRequest = chain.request().newBuilder().build();
            return chain.proceed(newRequest)
        }else{
            val subtype: String = requestBody?.contentType()?.subtype() ?:""
            if (subtype.contains("json")) {
                //modify every json request body
                val newProcessApplicationJsonRequestBody = processApplicationJsonRequestBody(requestBody!!)
                requestBody = newProcessApplicationJsonRequestBody
            }

            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", authHeader)
                    .post(requestBody)
                    .build()
            return chain.proceed(newRequest)
        }


    }

    private fun getPrivateKey(): PrivateKey? {
        val privateString = AppHandler.getmInstance(mContext).rsaKays.get(0)
        val decodePrivateKey = Base64.decode(privateString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(keySpec)
        return p
    }

    private fun getRowEnvlopeDecrytionKey(p: PrivateKey?, envlopeString: String): ByteArray? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher?.init(Cipher.DECRYPT_MODE, p)
        val envlopeDecode = Base64.decode(envlopeString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
        return rowEnvlopeDecrytionKey
    }

    @Throws(SignatureException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun calculateHMAC(data: String, key: String): String? {
        val HMAC_SHA512 = "HmacSHA256"
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA512)
        val mac: Mac = Mac.getInstance(HMAC_SHA512)
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(data.toByteArray()))
    }

    private fun toHexString(bytes: ByteArray): String? {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }


    private fun processApplicationJsonRequestBody(requestBody: RequestBody):  RequestBody? {
        val customReq: String? = bodyToString(requestBody)
        try {
            val mAppHandler = AppHandler.getmInstance(mContext)

            val obj = JSONObject(customReq)
            obj.put("deviceId", mAppHandler.androidID)
            obj.put("timestamp",""+DateUtils.getCurrentTimestamp())
            obj.put("format","json")

            getTokenBaseOnRSAlgorithm(obj)

            return RequestBody.create(requestBody.contentType(), obj.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getTokenBaseOnRSAlgorithm(obj: JSONObject) {
        val envlopeString = AppHandler.getmInstance(mContext).envlope

        val p = getPrivateKey()
        val rowEnvlopeDecrytionKey = getRowEnvlopeDecrytionKey(p, envlopeString)
        val envlpeDecryptionKey = Base64.encodeToString(rowEnvlopeDecrytionKey, Base64.DEFAULT)


        /* Decrypt the message, given derived encContentValues and initialization vector. */
        val sealedDataString = AppHandler.getmInstance(mContext).sealedData
        val sealDataDecode = Base64.decode(sealedDataString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)

        val secretKeySpec = SecretKeySpec(rowEnvlopeDecrytionKey, "RC4")
        val cipherRC4 = Cipher.getInstance("RC4") // Transformation of the algorithm
        cipherRC4.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val rowDecryptionKey = cipherRC4.doFinal(sealDataDecode)
        val sealDecryptionKey = Base64.encodeToString(rowDecryptionKey, Base64.NO_WRAP)
        val sealDecryptionKeyDecodeFormate = String(Base64.decode(sealDecryptionKey, Base64.NO_WRAP))


        val appsSecurityToken = AppHandler.getmInstance(mContext).appsSecurityToken
        val encodeAppsSecurityToken = Base64.encodeToString(appsSecurityToken.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)


        val toJson = obj.toString()

        val hmac: String? = calculateHMAC(toJson, sealDecryptionKeyDecodeFormate)

        val authDataString = "$encodeAppsSecurityToken:$hmac"

        authHeader = "Bearer " + Base64.encodeToString(authDataString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP);
    }

    private fun bodyToString(request: RequestBody): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

}
