package com.shon.kb.viewmodels

import android.bluetooth.le.ScanResult
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shon.kotlin_ble.scanner.scanDevices
import java.util.UUID

class MainViewModel:ViewModel() {

    private val serviceUUid: UUID = UUID.fromString("0000aa00-0000-1000-8000-00805f9b34fb")
    //    val filterUUID = ParcelUuid.fromString("0000aa00-0000-1000-8000-00805f9b34fb")
    private val writeUUid: UUID = UUID.fromString("0000aa02-0000-1000-8000-00805f9b34fb")
    private val notifyUUid: UUID = UUID.fromString("0000aa01-0000-1000-8000-00805f9b34fb")
    private val cacheList:MutableList<ScanResult> = mutableListOf()
    val scanList:MutableLiveData<List<ScanResult>> = MutableLiveData()
    fun scanDevice() {

        scanDevices{list->
            list.forEach {
                if (!cacheListContainsDevice(it)){
                    cacheList.add(it)
                }
            }
            scanList.postValue(cacheList.toList())
        }
    }

    private fun cacheListContainsDevice(scanResult: ScanResult):Boolean{
        var contains = false

        cacheList.forEachIndexed { index, cache ->
            if (TextUtils.equals(cache.device.address,scanResult.device.address)){
                contains = true
                cacheList[index] = scanResult
            }
        }
        return contains
    }
    fun connectDevice(){
//        viewModelScope.launch {
//            val connectDevice =
//                ClientBleGatt.connectDevice(
//                    this@MainActivity,
//                    it.device.address
//                )
//
//            val discoverServices = connectDevice?.discoverServices()
//            discoverServices?.let {
//                if (it){
//                    with(connectDevice) {
//                        enableCharacteristicNotification(serviceUUid, notifyUUid)
//
//                        delay(2000)
//                        val writeResult = writeCharacteristic(
//                            serviceUUid,
//                            writeUUid, byteArrayOf(0x02, 0x01)
//                        )
//                        Log.d("TAG", "writeResult: $writeResult")
//                    }
//                }
//            }
//        }
    }

}