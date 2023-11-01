package com.shon.kotlin_ble.api

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import com.shon.kotlin_ble.client.ClientGattEvent
import com.shon.kotlin_ble.core.KBLEScope
import com.shon.kotlin_ble.core.data.BleGattOperationStatus
import com.shon.kotlin_ble.core.data.BleWriteType
import com.shon.kotlin_ble.core.toHexString
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * @Author xiao
 * @Date 2023-10-20 17:12
 *
 */
class WriteCharacteristicApi(gatt: BluetoothGatt):CharacteristicApi(gatt) {

    private var writeResultCallback:((ByteArray)->Boolean)? = null
    private var writeResponseCallback :((ByteArray)->Boolean)? = null


    override fun onEvent(event: ClientGattEvent.CharacteristicEvent) {
        if (event is ClientGattEvent.CharacteristicWrite){
            if (event.status == BleGattOperationStatus.GATT_SUCCESS) {
                writeResultCallback?.invoke(event.characteristic.value)
            }
        }

        if (event is ClientGattEvent.CharacteristicChanged){
            writeResponseCallback?.invoke(event.characteristic.value)
        }

    }

    suspend fun writeWithoutResponse(characteristic: BluetoothGattCharacteristic, value: ByteArray):Boolean = suspendCoroutine { continuation->
        writeWithoutResponseFlow(characteristic, value).onEach {
            continuation.resume(it)
        }.launchIn(KBLEScope)
    }

    fun writeResponse(characteristic: BluetoothGattCharacteristic,value: ByteArray,dataCallBack:(ByteArray)->Boolean){

        callbackFlow {
            writeResponseCallback = {
                val finish = dataCallBack(it)
                if (finish){
                    trySend(true)
                    close()
                    true
                }
                false
            }
            writeCharacteristic(characteristic, value)
            awaitClose{writeResponseCallback = null}
        }.onEach {
        }.launchIn(KBLEScope)
    }




    private fun writeWithoutResponseFlow(characteristic: BluetoothGattCharacteristic, value: ByteArray):Flow<Boolean> = callbackFlow {
        writeResultCallback = {result->
            if (value.toHexString() == result.toHexString()){
                trySend(true)
                close()
                true
            }
            false
        }
        writeCharacteristic(characteristic, value)
        awaitClose{writeResultCallback = null}
    }


    @SuppressLint("MissingPermission")
    private fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, value: ByteArray){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeCharacteristic(characteristic, value, BleWriteType.DEFAULT.value)
        } else @Suppress("DEPRECATION") {
            characteristic.writeType = BleWriteType.DEFAULT.value
            characteristic.value = value
            gatt.writeCharacteristic(characteristic)
        }
    }
}