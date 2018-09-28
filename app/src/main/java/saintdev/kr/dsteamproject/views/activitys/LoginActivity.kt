package saintdev.kr.dsteamproject.views.activitys

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import saintdev.kr.dsteamproject.R
import saintdev.kr.dsteamproject.libs.functions.NetworkFunction
import saintdev.kr.dsteamproject.libs.functions.SharedPref
import saintdev.kr.dsteamproject.libs.netio.HttpManager
import saintdev.kr.dsteamproject.libs.netio.HttpURLs
import saintdev.kr.dsteamproject.libs.netio.libs.HttpRequestMap
import saintdev.kr.dsteamproject.libs.netio.libs.OnHttpListener
import saintdev.kr.dsteamproject.views.dialogs.CommonDialog
import saintdev.kr.dsteamproject.views.dialogs.openMessage

/**
 * Copyright (c) 2015 - 2018 Saint software All rights reserved.
 * 이 엑티비티는 앱이 실행되고 자동 로그인 또는 로그인 처리를 시도합니다.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var activity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.activity = this

        // 네트워크 작동 여부 확인
        if(!NetworkFunction.isEnableNetwork(this)) {
            "네트워크 연결이 없습니다.".openMessage("오류", this, DialogInterface.OnClickListener()
            { dialog, _ -> dialog.dismiss(); finish() })
            return
        } else {
            // 네트워크 연결 가능.
            // 현재 세션 키가 남아 있는지 확인함.
            SharedPref.openSharedPrep(this)
            val sessionKey = SharedPref.readLoginSession()

            if(sessionKey != null) {
                // 자동 로그인 가능
                // 자동 로그인 시작
                val dialog = CommonDialog.openProgress("로그인 중 입니다..", this)
                HttpManager.httpRequest(HttpURLs.AUTH_LOGIN_AUTO, OnAutoLoginCallback(dialog),
                        HttpRequestMap("session", sessionKey))

                return
            }
        }

        // 로그인 에 필요한 ui 시작.
        login_confirm.setOnClickListener {
            val userId = login_auth_uid.text.toString()
            val userPass = login_auth_passwd.text.toString()

            if(userId.isEmpty() || userPass.isEmpty()) {
                "입력하지 않은 값이 있습니다.".openMessage("빈 필드", this@LoginActivity)
            } else {
                // 로그인 시도
                val dialog = CommonDialog.openProgress("로그인 중 입니다..", this@LoginActivity)
                HttpManager.httpRequest(HttpURLs.AUTH_LOGIN, OnLoginCallback(dialog),
                        HttpRequestMap("username", userId),
                        HttpRequestMap("password", userPass))
            }
        }
    }

    /**
     * Auto Login callback.
     * 닫고싶은 프로그레스 다이얼로그를 인자로 전송함.
     */
    inner class OnAutoLoginCallback(val dialog: ProgressDialog?) : OnHttpListener {
        override fun onRequested(obj: JSONObject) {
            dialog?.dismiss()

            if(obj.getBoolean("success")) {
                // 새로운 세션 값이 있다면 여기서 등록하십시오.
                openMainActivity()
            } else {
                // 잘못된 세션 값 입니다.
                "자동 로그인에 실패했습니다.\n다시 로그인 해 주세요.".openMessage("오류", this@LoginActivity)
            }
        }

        override fun onFailed() {
            dialog?.dismiss()
            // 서버 오류
            "서버 오류가 발생했습니다.".openMessage("서버 오류", this@LoginActivity)
        }
    }

    /**
     * Login callback
     * 닫고싶은 프로그레스 다이얼로그를 인자로 전송함.
     */
    inner class OnLoginCallback(val dialog: ProgressDialog?) : OnHttpListener {
        override fun onRequested(obj: JSONObject) {
            dialog?.dismiss()

            if(obj.getBoolean("success")) {
                // 로그인 성공 여부를 확인
                val session = obj.getString("session")
                SharedPref.writeLoginSession(session)       // Write session.
                openMainActivity()
            } else {
                val errorMsg = obj.getString("message")
                "로그인에 실패했습니다\n$errorMsg".openMessage("오류", this@LoginActivity)
            }
        }

        override fun onFailed() {
            dialog?.dismiss()
            "서버 오류가 발생했습니다.".openMessage("서버 오류", this@LoginActivity)
        }
    }

    fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}