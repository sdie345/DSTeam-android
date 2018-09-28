package saintdev.kr.dsteamproject.libs.netio.libs

import org.json.JSONObject

interface OnHttpListener {
    fun onRequested(obj: JSONObject)
    fun onFailed()
}