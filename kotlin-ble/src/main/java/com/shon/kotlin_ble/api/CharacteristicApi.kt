package com.shon.kotlin_ble.api

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicChanged
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicEvent
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicRead
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicWrite
import com.shon.kotlin_ble.core.data.BleWriteType
import com.shon.kotlin_ble.core.toHexString
import java.util.UUID

/**
 *
 * @Author xiao
 * @Date 2023-10-20 11:07
 *
 */
open class CharacteristicApi(gatt: BluetoothGatt):GattApi<CharacteristicEvent>(gatt) {

    private var readResultCallBack:((ByteArray)->Boolean)? = null
    private var writeResultCallback:((ByteArray)->Boolean)? = null
    private var writeResponseCallback:((ByteArray)->Unit)? = null
    override fun onEvent(event: CharacteristicEvent) {
        when(event){

            is CharacteristicChanged->{
                Log.d("CharacteristicApi", "value = ${event.value.value.toHexString()} ")
                writeResponseCallback?.invoke(event.value.value)
            }
            else->{}
        }
    }

     @SuppressLint("MissingPermission")
     fun writeWithResult(serviceUUID: UUID,
                         characteristicUUID: UUID,
                         value: ByteArray,callBack:(ByteArray)->Boolean){
         writeResultCallback = callBack
         writeCharacteristic(serviceUUID, characteristicUUID, value)
    }



    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun writeCharacteristic(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        value: ByteArray
    ) {

        val characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeCharacteristic(characteristic, value, BleWriteType.DEFAULT.value)
        } else @Suppress("DEPRECATION") {
            characteristic.writeType = BleWriteType.DEFAULT.value
            characteristic.value = value
            gatt.writeCharacteristic(characteristic)
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun enableCharacteristicNotification(
        characteristic:BluetoothGattCharacteristic
    ) {
        gatt.setCharacteristicNotification(characteristic, true)
         val descriptorUUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
        val descriptor = characteristic.getDescriptor(descriptorUUID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        } else @Suppress("DEPRECATION") {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disableCharacteristicNotification(
        serviceUUID: UUID,
        characteristicUUID: UUID
    ) {
        val characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
        gatt.setCharacteristicNotification(characteristic, false)
    }

    fun clearWriteCallback() {
        readResultCallBack = null
    }


}