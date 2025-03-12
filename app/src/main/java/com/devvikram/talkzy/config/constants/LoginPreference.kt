package com.devvikram.talkzy.config.constants

import android.content.Context
import androidx.core.content.edit

class LoginPreference(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setUserId(userId: String) {
        sharedPreferences.edit() { putString(USER_ID, userId) }
    }

    fun getUserId(): String {
        return sharedPreferences.getString(USER_ID, "").toString()
    }

    fun setUserName(userName: String) {
        sharedPreferences.edit() { putString(USER_NAME, userName) }
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(USER_NAME, null)
    }

    fun setUserEmail(userEmail: String) {
        sharedPreferences.edit() { putString(USER_EMAIL, userEmail) }
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(USER_EMAIL, null)
    }

    fun setSessionId(sessionId: String) {
        sharedPreferences.edit() { putString(SESSION_ID, sessionId) }
    }

    fun getSessionId(): String? {
        return sharedPreferences.getString(SESSION_ID, null)
    }

    fun setDeviceId(deviceId: String) {
        sharedPreferences.edit() { putString(DEVICE_ID, deviceId) }
    }

    fun getDeviceId(): String? {
        return sharedPreferences.getString(DEVICE_ID, null)

    }

    fun setFcmToken(fcmToken: String) {
        sharedPreferences.edit() { putString(FCM_TOKEN, fcmToken) }
    }

    fun getFcmToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN, null)
    }


    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit() { putBoolean(IS_LOGGED_IN, isLoggedIn) }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun setOnBoardingCompleted(isOnBoardingCompleted: Boolean) {
        sharedPreferences.edit() { putBoolean(IS_ONBOARD_COMPLETED, isOnBoardingCompleted) }
    }
    fun isOnBoardingCompleted(): Boolean? {
        return sharedPreferences.getBoolean(IS_ONBOARD_COMPLETED, false)
    }


    companion object {
        const val PREFERENCES_NAME = "LOGIN_PREFERENCES"
        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val USER_ID = "USER_ID"
        private const val USER_NAME = "USER_NAME"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val SESSION_ID = "SESSION_ID"
        private const val DEVICE_ID = "DEVICE_ID"
        private const val FCM_TOKEN = "FCM_TOKEN"
        private const val IS_ONBOARD_COMPLETED = "IS_ONBOARD_COMPLETED"

    }
}