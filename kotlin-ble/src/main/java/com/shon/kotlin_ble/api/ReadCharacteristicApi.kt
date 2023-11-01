package com.shon.kotlin_ble.api

import android.Manifest
import android.bluetooth.BluetoothGatt
import androidx.annotation.RequiresPermission
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicRead
import java.util.UUID

/**
 *
 * @Author xiao
 * @Date 2023-10-20 11:07
 *
 */
open class ReadCharacteristicApi(gatt: BluetoothGatt):GattApi<CharacteristicRead>(gatt) {

    private var readResultCallBack:((ByteArray)->Boolean)? = null
    override fun onEvent(event: CharacteristicRead) {
        readResultCallBack?.invoke(event.characteristic.value)
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun readCharacteristic(
        serviceUUID: UUID,
        characteristicUUID: UUID
    ) {
        val characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
        gatt.readCharacteristic(characteristic)
    }



}