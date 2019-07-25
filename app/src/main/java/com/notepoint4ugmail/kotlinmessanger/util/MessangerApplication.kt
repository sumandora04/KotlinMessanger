package com.notepoint4ugmail.kotlinmessanger.util

import android.app.Application
import timber.log.Timber

class MessangerApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}