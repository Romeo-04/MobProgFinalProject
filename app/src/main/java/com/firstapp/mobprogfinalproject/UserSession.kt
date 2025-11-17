package com.firstapp.mobprogfinalproject

import android.content.Context
import android.content.SharedPreferences

object UserSession {
    private const val PREF_NAME = "pandog_user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(context: Context, userId: Int, userName: String, userEmail: String) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getUserId(context: Context): Int {
        return getPreferences(context).getInt(KEY_USER_ID, -1)
    }

    fun getUserName(context: Context): String {
        return getPreferences(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getUserEmail(context: Context): String {
        return getPreferences(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}
