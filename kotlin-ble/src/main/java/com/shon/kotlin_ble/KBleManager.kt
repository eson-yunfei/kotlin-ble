package com.shon.kotlin_ble

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import com.shon.kotlin_ble.core.KBLEScope
import com.shon.kotlin_ble.scanner.KBleScanner
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class KBleManager private constructor(){

    companion object{
        val instance by lazy { KBleManager() }
    }

    private var mContext:Context? = null
    private  var nativeBleManager:BluetoothManager? = null
    private  var kBleScanner: KBleScanner? = null
    fun initManager(context: Context){
        mContext = context

    }

    private fun initNativeManager(){
        mContext?.let {cxt->
            nativeBleManager?: kotlin.run {
                nativeBleManager = cxt.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            }

            nativeBleManager?.let {
                kBleScanner = KBleScanner(it.adapter.bluetoothLeScanner)
            }

        }
    }

    fun startScan(result:(List<ScanResult>)->Unit) {
        initNativeManager()
        kBleScanner?.startScan()?.onEach {
            result(it)
        }?.launchIn(KBLEScope)
    }
}