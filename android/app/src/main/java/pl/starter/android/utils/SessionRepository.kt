package pl.starter.android.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

interface SessionRepository {
    fun isLoggedIn(): Boolean
}

const val API_TOKEN = "api_token"

class SessionRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth) :
    SessionRepository {
    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser?.isAnonymous == false
    }


}
