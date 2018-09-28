package saintdev.kr.dsteamproject.libs.functions

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPref {
    val LOGIN_SESSION = "login.session"

    private var sharedPref: SharedPreferences? = null

    fun openSharedPrep(context: Context) {
        this.sharedPref = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE)
    }

    /**
     * sharedPref 에 login session 을 작성 한다.
     */
    fun writeLoginSession(data: String) {
        if(this.sharedPref == null) {
            Log.e("DSMAD", "Plz start openSahredPrep() !")
        } else {
            sharedPref?.edit()?.putString("session", data)?.apply()
        }
    }

    /**
     * sharedPref 의 login session 을 가져온다.
     */
    fun readLoginSession() : String? {
        return if(this.sharedPref == null) {
            Log.e("DSMAD", "Plz start openSahredPrep() !")
            null
        } else sharedPref?.getString("session", null)
    }
}