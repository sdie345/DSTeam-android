package saintdev.kr.dsteamproject.libs.functions

import android.content.Context
import android.net.ConnectivityManager

object NetworkFunction {
    /**
     * Device 가 Network 연결이 가능한 상태인지 확인한다.
     */
    fun isEnableNetwork(context: Context) : Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connManager.activeNetworkInfo
        return netInfo.isConnected
    }
}