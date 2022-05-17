package com.example.testapp.utils

import android.content.SharedPreferences

class TokenManager(private val prefs: SharedPreferences) {
    private val editor: SharedPreferences.Editor = prefs.edit()
    fun saveToken(token: String) {
        editor.putString("ACCESS_TOKEN", token).commit()
    }

    fun deleteToken() {
        editor.remove("ACCESS_TOKEN").commit()
    }

    val token: String get() = prefs.getString("ACCESS_TOKEN", "")!!


    companion object {
        private var INSTANCE: TokenManager? = null

        @Synchronized
        fun getInstance(prefs: SharedPreferences): TokenManager? {
            if (INSTANCE == null) {
                INSTANCE = TokenManager(prefs)
            }
            return INSTANCE
        }
    }

}