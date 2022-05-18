package com.example.testapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.api.ApiClient
import com.example.testapp.api.ApiService
import com.example.testapp.api.models.ResponseLogin
import com.example.testapp.utils.TokenManager
import kotlinx.android.synthetic.main.activity_logen.*
import retrofit2.Call
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    private lateinit var service: ApiService
    private lateinit var callLogin: Call<ResponseLogin>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logen)
        service = ApiClient.createService(ApiService::class.java)
        btn_submit.setOnClickListener {
            progress.visibility=View.VISIBLE

            if (user_login.text.isEmpty()) {
                user_login.requestFocus()
                progress.visibility=View.GONE
                btn_submit.isClickable=true
                error_login.text = "Please imput login"
                error_emali_layout.visibility = View.VISIBLE

            } else if (user_password.text.isEmpty()) {
                user_password.requestFocus()
                progress.visibility=View.GONE
                btn_submit.isClickable=true
                error_emali_layout.visibility = View.GONE
                error_password.text = "Please imput password"
                error_passw_or_login.visibility = View.VISIBLE

            } else {
                error_passw_or_login.visibility = View.GONE
                error_emali_layout.visibility = View.GONE
                progress.visibility = View.VISIBLE
                btn_submit.isEnabled = true

                val map: HashMap<String, Any> = HashMap()
                map["login"] = user_login.text.toString()
                map["password"] = user_password.text.toString()


                callLogin = service.login(map)
                Log.d("Url", callLogin.request().url.toString())

                callLogin.enqueue(object : Callback<ResponseLogin> {
                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        if (callLogin.isCanceled) {
                            return
                        }
                        progress.visibility = View.GONE
                        Log.d("error", t.message.toString())

                    }

                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: retrofit2.Response<ResponseLogin>
                    ) {
                        progress.visibility = View.GONE
                        if (response.isSuccessful) {
                            TokenManager.getInstance(
                                applicationContext.getSharedPreferences(
                                    "prefs",
                                    Context.MODE_PRIVATE
                                )
                            )
                                ?.saveToken(response.body()?.token.toString())
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }else if (response.message().toString().toUpperCase().equals("BAD REQUEST")){
                            error_main.visibility=View.VISIBLE
                            error_main_txt.text="login/password should not be empty"
                        }else if (response.message().toString().toUpperCase().equals("NOT FOUND")){
                            error_main.visibility=View.VISIBLE
                            error_main_txt.text="Invalid login or password"
                        }

                    }
                })


            }
        }


    }
}

