package com.shon.kotlin_ble

import android.bluetooth.BluetoothManager
import android.content.Context
import com.shon.kotlin_ble.scanner.KBleScanner

class KBleManager private constructor() {

    companion object {
        val instance by lazy { KBleManager() }
    }

    private lateinit var mContext:Context
    private var nativeBleManager: BluetoothManager? = null
    private var kBleScanner: KBleScanner? = null
    fun initManager(context: Context) {
        mContext = context

    }

    fun getNativeManager():BluetoothManager{
        if (nativeBleManager == null) {
            synchronized(instance) {
                if (nativeBleManager == null) {
                    nativeBleManager =
                        mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                }

            }
        }
        return nativeBleManager!!;
    }

}