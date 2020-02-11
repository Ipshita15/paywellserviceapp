package com.cloudwell.paywell.services.retrofit

import android.content.Context
import android.util.Base64
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import org.json.JSONObject
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-02-10.
 */
 class RSAUtilty{

    companion object {

        public fun getTokenBaseOnRSAlgorithm(obj: JSONObject): String {
            val envlopeString = AppHandler.getmInstance(AppController.getContext()).envlope

            val p = RSAUtilty.Companion.getPrivateKey(AppController.getContext())
            val rowEnvlopeDecrytionKey = getRowEnvlopeDecrytionKey(p, envlopeString)
            val envlpeDecryptionKey = Base64.encodeToString(rowEnvlopeDecrytionKey, Base64.DEFAULT)


            /* Decrypt the message, given derived encContentValues and initialization vector. */
            val sealedDataString = AppHandler.getmInstance(AppController.getContext()).sealedData
            val sealDataDecode = Base64.decode(sealedDataString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)

            val secretKeySpec = SecretKeySpec(rowEnvlopeDecrytionKey, "RC4")
            val cipherRC4 = Cipher.getInstance("RC4") // Transformation of the algorithm
            cipherRC4.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val rowDecryptionKey = cipherRC4.doFinal(sealDataDecode)
            val sealDecryptionKey = Base64.encodeToString(rowDecryptionKey, Base64.NO_WRAP)
            val sealDecryptionKeyDecodeFormate = String(Base64.decode(sealDecryptionKey, Base64.NO_WRAP))


            val appsSecurityToken = AppHandler.getmInstance(AppController.getContext()).appsSecurityToken
            val encodeAppsSecurityToken = Base64.encodeToString(appsSecurityToken.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)


            val toJson = obj.toString()

            AppController.previousRequestObject = toJson

            val hmac: String? = calculateHMAC(toJson, sealDecryptionKeyDecodeFormate)

            val authDataString = "$encodeAppsSecurityToken:$hmac"

             return "Bearer " + Base64.encodeToString(authDataString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP);
        }




        fun getPrivateKey(mContext: Context): PrivateKey? {
            val privateString = AppHandler.getmInstance(mContext).rsaKays.get(0)
            val decodePrivateKey = Base64.decode(privateString, Base64.DEFAULT)
            val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
            val kf: KeyFactory = KeyFactory.getInstance("RSA")
            val p = kf.generatePrivate(keySpec)
            return p
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

        private fun getRowEnvlopeDecrytionKey(p: PrivateKey?, envlopeString: String): ByteArray? {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher?.init(Cipher.DECRYPT_MODE, p)
            val envlopeDecode = Base64.decode(envlopeString.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
            val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
            return rowEnvlopeDecrytionKey
        }

    }
}