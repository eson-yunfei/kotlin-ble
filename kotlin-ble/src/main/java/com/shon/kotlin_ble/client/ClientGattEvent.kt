package com.shon.kotlin_ble.client

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.shon.kotlin_ble.core.DataByteArray
import com.shon.kotlin_ble.core.data.BleGattConnectionStatus
import com.shon.kotlin_ble.core.data.BleGattOperationStatus
import com.shon.kotlin_ble.core.data.GattConnectionState

/**
 *
 * @Author xiao
 * @Date 2023-10-19 11:20
 *
 */
sealed interface ClientGattEvent {

    sealed interface ServiceEvent : ClientGattEvent
    sealed interface CharacteristicEvent : ServiceEvent
    sealed interface DescriptorEvent : ServiceEvent

    data class ConnectionStateChanged(
        val gatt:BluetoothGatt?,
        val status: BleGattConnectionStatus,
        val newState: GattConnectionState
    ) : ClientGattEvent


    data class ServicesDiscovered(
        val services: List<BluetoothGattService>,
        val status: BleGattOperationStatus
    ) : ClientGattEvent

    data class CharacteristicChanged(
        val characteristic: BluetoothGattCharacteristic,
        val value: DataByteArray
    ) : CharacteristicEvent

    data class CharacteristicRead(
        val characteristic: BluetoothGattCharacteristic,
        val value: DataByteArray,
        val status: BleGattOperationStatus
    ) : CharacteristicEvent

    data class CharacteristicWrite(
        val characteristic: BluetoothGattCharacteristic,
        val status: BleGattOperationStatus
    ) : CharacteristicEvent


    data class DescriptorRead(
        val descriptor: BluetoothGattDescriptor,
        val value: DataByteArray,
        val status: BleGattOperationStatus
    ) : DescriptorEvent

    data class DescriptorWrite(
        val descriptor: BluetoothGattDescriptor,
        val status: BleGattOperationStatus
    ) : DescriptorEvent


    data class MtuChanged(val mtu: Int, val status: BleGattOperationStatus) : ClientGattEvent
}