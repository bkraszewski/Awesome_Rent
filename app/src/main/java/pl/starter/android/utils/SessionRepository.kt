package pl.starter.android.utils

import android.content.SharedPreferences

interface SessionRepository {
    fun saveToken(token: String)
    fun getToken(): String
    fun removeToken()
}

const val API_TOKEN = "api_token"

class SessionRepositoryImpl constructor(private val sharedPreferences: SharedPreferences) :
    SessionRepository {

    override fun removeToken() {
        sharedPreferences.edit().remove(API_TOKEN).apply()
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(API_TOKEN, token).apply()
    }

    override fun getToken(): String {
        return sharedPreferences.getString(API_TOKEN, "")
    }
}
