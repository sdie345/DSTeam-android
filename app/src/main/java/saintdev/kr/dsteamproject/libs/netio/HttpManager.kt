package saintdev.kr.dsteamproject.libs.netio

import android.os.AsyncTask
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import saintdev.kr.dsteamproject.libs.netio.libs.HttpRequestMap
import saintdev.kr.dsteamproject.libs.netio.libs.OnHttpListener
import java.lang.Exception

object HttpManager {
    private val MEDIA_TYPE = MediaType.parse("application/json")

    /**
     * Http Listen
     */
    fun httpRequest(url: String, listener: OnHttpListener, vararg args: HttpRequestMap) {
        val body = RequestBody.create(MEDIA_TYPE, makeJSONString(args))
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        BackgroundTask(request, listener).execute()
    }

    /**
     * Create json string from HttpRequestMap
     */
    private fun makeJSONString(args: Array<out HttpRequestMap>) : String {
        val stringBuf = StringBuffer()

        args.forEach {
            val obj = JSONObject().put(it.key, it.data)
            stringBuf.append(obj.toString())
        }

        return stringBuf.toString()
    }

    private class BackgroundTask(val request: Request, val listener: OnHttpListener) : AsyncTask<Void, Void, String?>() {
        private val client = OkHttpClient()

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                val result = client.newCall(request).execute()
                if (result.code() != 200) return null
                return result?.body()?.string()
            } catch(ex: Exception) {
                ex.printStackTrace()
                return null
            }
        }


        override fun onPostExecute(data: String?) {
            if(data == null) {
                listener.onFailed()
            } else {
                // data == JSONObject
                try {
                    listener.onRequested(JSONObject(data))
                } catch (ex: JSONException) {
                    // JSON 데이터가 아님.
                    listener.onFailed()
                }
            }
        }
    }
}

object HttpURLs {
    val AUTH_LOGIN_AUTO = "http://58.145.101.15:3001/auth/autologin"    // 자동 로그인 eop
    val AUTH_LOGIN = "http://58.145.101.15:3001/auth/login"             // 로그인 eop
}