package com.shon.kotlin_ble.core

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanFilter
import com.shon.kotlin_ble.scanner.BleScanFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.UUID

/**
 *
 * @Author xiao
 * @Date 2023-10-20 15:11
 *
 */

val KBLEScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)




fun BluetoothGatt.getGattService(serviceUUID: UUID): BluetoothGattService = getService(serviceUUID)

fun BluetoothGatt.getGattCharacteristic(
    serviceUUID: UUID,
    characteristicUUID: UUID
): BluetoothGattCharacteristic = getService(serviceUUID).getCharacteristic(characteristicUUID)

fun List<BleScanFilter>.toNative(): List<ScanFilter> = map { bleScanFilter ->

    val builder = ScanFilter.Builder()
    bleScanFilter.deviceName?.let {
        builder.setDeviceName(it)
    }
    bleScanFilter.deviceAddress?.let {
        builder.setDeviceAddress(it)
    }
    bleScanFilter.serviceUuid?.let {
        builder.setServiceUuid(it)
    }
    builder.build()
}