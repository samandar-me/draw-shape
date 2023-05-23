package com.sdk.drawshape.app

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal

class App : Application() {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        oneSignal()
    }

    private fun oneSignal() {
        OneSignal.initWithContext(this)
        OneSignal.setAppId("db86f6d7-b322-455b-9468-b281ce619559")
        OneSignal.promptForPushNotifications()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    }
}