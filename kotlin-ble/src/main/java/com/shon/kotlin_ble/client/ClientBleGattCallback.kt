package com.shon.kotlin_ble.client

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicChanged
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicRead
import com.shon.kotlin_ble.client.ClientGattEvent.CharacteristicWrite
import com.shon.kotlin_ble.client.ClientGattEvent.ConnectionStateChanged
import com.shon.kotlin_ble.client.ClientGattEvent.DescriptorRead
import com.shon.kotlin_ble.client.ClientGattEvent.DescriptorWrite
import com.shon.kotlin_ble.client.ClientGattEvent.MtuChanged
import com.shon.kotlin_ble.client.ClientGattEvent.ServicesDiscovered
import com.shon.kotlin_ble.core.DataByteArray
import com.shon.kotlin_ble.core.data.BleGattConnectionStatus
import com.shon.kotlin_ble.core.data.BleGattOperationStatus
import com.shon.kotlin_ble.core.data.GattConnectionState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 *
 * @Author xiao
 * @Date 2023-10-19 11:19
 *
 */
class ClientBleGattCallback private constructor(): BluetoothGattCallback() {

    companion object{
        val gattCallback by lazy { ClientBleGattCallback() }
    }

    private val _event = MutableSharedFlow<ClientGattEvent>(
        extraBufferCapacity = 1000, //Warning: because of this parameter we can miss notifications
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        _event.tryEmit(
            ConnectionStateChanged(
                gatt,
                BleGattConnectionStatus.create(status),
                GattConnectionState.create(newState)
            )
        )
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        _event.tryEmit(
            ServicesDiscovered(
                gatt.services,
                BleGattOperationStatus.create(status)
            )
        )
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        characteristic?.let {
            _event.tryEmit(
                CharacteristicWrite(
                    it,
                    BleGattOperationStatus.create(status)
                )
            )
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray,
        status: Int
    ) {
            _event.tryEmit(
                CharacteristicRead(
                    characteristic, DataByteArray(characteristic.value),
                    BleGattOperationStatus.create(status)
                )
            )

    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {
        _event.tryEmit(CharacteristicChanged(characteristic, DataByteArray(value)))
    }


    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        characteristic?.let {
            _event.tryEmit(CharacteristicChanged(it, DataByteArray(it.value)))
        }
    }


    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        descriptor?.let {
            _event.tryEmit(DescriptorWrite(it, BleGattOperationStatus.create(status)))
        }
    }

    override fun onDescriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        descriptor?.let {
            _event.tryEmit(
                DescriptorRead(
                    it,
                    DataByteArray(it.value),
                    BleGattOperationStatus.create(status)
                )
            )
        }
    }

    override fun onDescriptorRead(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int,
        value: ByteArray
    ) {
        _event.tryEmit(
            DescriptorRead(
                descriptor,
                DataByteArray(value),
                BleGattOperationStatus.create(status)
            )
        )
    }


    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        _event.tryEmit(MtuChanged(mtu, BleGattOperationStatus.create(status)))
    }


}