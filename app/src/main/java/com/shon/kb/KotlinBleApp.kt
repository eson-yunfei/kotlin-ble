package com.shon.kb

import android.app.Application
import com.shon.kotlin_ble.KBleManager

class KotlinBleApp:Application() {

    override fun onCreate() {
        super.onCreate()
        KBleManager.instance.initManager(this)
    }
}